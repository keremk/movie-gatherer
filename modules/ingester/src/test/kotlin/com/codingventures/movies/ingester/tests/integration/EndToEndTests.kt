package com.codingventures.movies.ingester.tests.integration

import com.codingventures.movies.containers.MockTmdbServer
import com.codingventures.movies.containers.TestKafkaSystem
import com.codingventures.movies.domain.*
import com.codingventures.movies.ingester.config.KafkaTopicsProvider
import com.codingventures.movies.ingester.pipeline.Pipeline
import com.codingventures.movies.ingester.producer.Producers
import com.codingventures.movies.ingester.producer.TaskProducer
import com.codingventures.movies.ingester.remote.tmdb.config.RemoteConfigProvider
import com.codingventures.movies.ingester.remote.tmdb.tasks.personDetailsFetchTask
import io.kotest.core.spec.Spec
import io.kotest.core.spec.style.ShouldSpec
import com.codingventures.movies.ingester.remote.tmdb.fetchers.TmdbClient
import com.codingventures.movies.ingester.remote.tmdb.tasks.popularMoviesFetchTask
import com.codingventures.movies.kafka.ConsumerSettings
import com.codingventures.movies.kafka.KafkaConfigProvider
import com.codingventures.movies.kafka.KafkaRunner
import com.sksamuel.avro4k.Avro
import io.kotest.assertions.throwables.shouldNotThrow
import io.kotest.core.spec.IsolationMode
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.util.KtorExperimentalAPI
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import org.apache.avro.generic.GenericRecord
import org.apache.kafka.clients.consumer.ConsumerRecords
import org.apache.kafka.clients.producer.KafkaProducer

@KtorExperimentalAPI
@ExperimentalCoroutinesApi
class IngesterTests : ShouldSpec() {
    private lateinit var remoteConfigProvider: RemoteConfigProvider
    private lateinit var sutRunner: Pipeline
    private lateinit var moviesListener: KafkaRunner
    private lateinit var peopleListener: KafkaRunner
    private lateinit var producers: Producers

    private val kafkaTopicsProvider = KafkaTopicsProvider(
        movies = "movies",
        people = "people",
        deadLetters = "deadletters",
        tasks = "tasks"
    )

    override fun isolationMode(): IsolationMode? = IsolationMode.InstancePerTest

    @KtorExperimentalAPI
    override fun beforeSpec(spec: Spec) {
        super.beforeSpec(spec)
        initalizeMockTmdbServer()
        initializeKafkaSystem()
    }

    @KtorExperimentalAPI
    private fun initalizeMockTmdbServer() {
        val tmdbServer = MockTmdbServer.initialize()
        remoteConfigProvider = RemoteConfigProvider(
            host = tmdbServer.host,
            port = tmdbServer.serverPort,
            https = false,
            apiKey = "TestKey"
        )
    }

    @KtorExperimentalAPI
    private fun createTmdbClient(remoteConfigProvider: RemoteConfigProvider): TmdbClient {
        val httpClient = HttpClient(CIO) {
            install(JsonFeature) {
                serializer = KotlinxSerializer(
                    Json(JsonConfiguration.Stable.copy(isLenient = true, ignoreUnknownKeys = true))
                )
            }
        }
        return TmdbClient(httpClient, remoteConfigProvider)
    }

    @KtorExperimentalAPI
    private fun initializeKafkaSystem() {
        val serverConfig = TestKafkaSystem.initialize(kafkaTopicsProvider.toList())

        val sutKafkaConfigProvider = KafkaConfigProvider(
            serverConfig = serverConfig,
            consumerSettings = ConsumerSettings(
                enableAutoCommit = false,
                maxPollRecords = 10,
                consumerGroupId = "sut-kafka-consumer"
            )
        )
        sutRunner = Pipeline.initialize(
            kafkaConfigProvider = sutKafkaConfigProvider,
            kafkaTopicsProvider = kafkaTopicsProvider,
            remoteConfigProvider = remoteConfigProvider
        )

        val kafkaProducer = KafkaProducer<String, GenericRecord>(sutKafkaConfigProvider.producerProperties())
        producers = Producers(kafkaProducer, kafkaTopicsProvider)

        val moviesListenerConfigProvider = KafkaConfigProvider(
            serverConfig = serverConfig,
            consumerSettings = ConsumerSettings(
                enableAutoCommit = true,
                maxPollRecords = 1, // IMPORTANT - otherwise test will not count entries correctly
                consumerGroupId = "movies-listener"
            )
        )
        moviesListener = KafkaRunner.initialize(moviesListenerConfigProvider)

        val peopleListenerConfigProvider = KafkaConfigProvider(
            serverConfig = serverConfig,
            consumerSettings = ConsumerSettings(
                enableAutoCommit = true,
                maxPollRecords = 1, // IMPORTANT - otherwise test will not count entries correctly
                consumerGroupId = "people-listener"
            )
        )
        peopleListener = KafkaRunner.initialize(peopleListenerConfigProvider)
    }

    private suspend fun verifyPeopleList(
        expectedPeople: Map<Urn, Pair<String, Int>>,
        channel: Channel<ConsumerRecords<String, GenericRecord>>
    ) {
        val peopleCount = expectedPeople.values.fold(0) { acc, value ->
            acc + value.second
        }

        shouldNotThrow<TimeoutCancellationException> {
            withTimeout(30000L) {
                // We need to make sure max.poll.records == 1 for the count to work
                val occurances: Map<Urn, Pair<String, Int>> = (0 until peopleCount).fold(mutableMapOf()) { occurance, _ ->
                    val records = channel.receive()
                    records.forEach {
                        val person = Avro.default.fromRecord(PersonDetails.serializer(), it.value())
                        val currentCount = occurance[person.personUrn]?.second ?: 0
                        val newCount = currentCount + 1
                        occurance.apply { put(person.personUrn, Pair(person.name, newCount)) }
                    }
                    occurance
                }
                expectedPeople.forEach {
                    val occuranceCount = occurances[it.key]
                    occuranceCount shouldNotBe null
                    occuranceCount?.first shouldBe it.value.first
                    occuranceCount?.second shouldBe it.value.second
                }
            }
        }
    }

    private suspend fun verifyMovieList(
        expectedMovies: Map<Urn, Pair<String, Int>>,
        moviesChannel: Channel<ConsumerRecords<String, GenericRecord>>
    ) {
        val movieCount = expectedMovies.values.fold(0) { acc, value ->
            acc + value.second
        }

        shouldNotThrow<TimeoutCancellationException> {
            withTimeout(30000L) {
                val occurances: Map<Urn, Pair<String, Int>> =
                    (0 until movieCount).fold(mutableMapOf()) { occurance, _ ->
                        val records = moviesChannel.receive()
                        records.forEach {
                            val movie = Avro.default.fromRecord(MovieDetails.serializer(), it.value())
                            val currentCount = occurance[movie.movieUrn]?.second ?: 0
                            val newCount = currentCount + 1
                            occurance.apply { put(movie.movieUrn, Pair(movie.title, newCount)) }
                        }
                        occurance
                    }
                expectedMovies.forEach {
                    val occuranceCount = occurances[it.key]
                    occuranceCount shouldNotBe null
                    occuranceCount?.first shouldBe it.value.first
                    occuranceCount?.second shouldBe it.value.second
                }
            }
        }
    }

    init {
        should("Call the mock server") {
            val fetchTask = personDetailsFetchTask("2176")
            val tmdbClient = createTmdbClient(remoteConfigProvider)
            val response = tmdbClient.fetchData(fetchTask)

            response shouldNotBe null
        }

        should("Publish movies and people to Kafka topics") {
            val fetchTask = popularMoviesFetchTask(1)
            producers.produce(TaskProducer(listOf(fetchTask), kafkaTopicsProvider.tasks))

            val sutJob = launch {
                sutRunner.run()
            }
            val moviesChannel = Channel<ConsumerRecords<String, GenericRecord>>()
            val moviesJob = launch(Dispatchers.IO) {
                moviesListener.run(listOf(kafkaTopicsProvider.movies), moviesChannel)
            }
            val peopleChannel = Channel<ConsumerRecords<String, GenericRecord>>()
            val peopleJob = launch(Dispatchers.IO) {
                peopleListener.run(listOf(kafkaTopicsProvider.people), peopleChannel)
            }

            val expectedMovies = mapOf(
                createMovieUrn(419704) to Pair("Ad Astra", 1),
                createMovieUrn(475557) to Pair("Joker", 1),
                createMovieUrn(496243) to Pair("Parasite", 1))
            verifyMovieList(expectedMovies, moviesChannel)

            val expectedPeople = mapOf(
                createPersonUrn(287) to Pair("Brad Pitt", 1),
                createPersonUrn(2176) to Pair("Tommy Lee Jones", 1),
                createPersonUrn(17018) to Pair("Ruth Negga", 1),
                createPersonUrn(20561) to Pair("James Gray", 1),
                createPersonUrn(73421) to Pair("Joaquin Phoenix", 1),
                createPersonUrn(1545693) to Pair("Zazie Beetz", 1),
                createPersonUrn(57130) to Pair("Todd Phillips", 1),
                createPersonUrn(20738) to Pair("Song Kang-ho", 1),
                createPersonUrn(21684) to Pair("Bong Joon-ho", 1)
            )
            verifyPeopleList(expectedPeople, peopleChannel)

            sutJob.cancel()
            moviesJob.cancel()
            peopleJob.cancel()
        }

        should("Skip tasks if they fail while not losing other non-failing tasks") {
            val fetchTask = popularMoviesFetchTask(3) // Page 3 is set to be failing
            producers.produce(TaskProducer(listOf(fetchTask), kafkaTopicsProvider.tasks))

            val sutJob = launch {
                sutRunner.run()
            }

            val peopleChannel = Channel<ConsumerRecords<String, GenericRecord>>()
            val peopleJob = launch(Dispatchers.IO) {
                peopleListener.run(listOf(kafkaTopicsProvider.people), peopleChannel)
            }

            // Brad Pitt's profile will give error, so should not be in the list, but rest are here.
            val expectedPeople = mapOf(
                createPersonUrn(2176) to Pair("Tommy Lee Jones", 1),
                createPersonUrn(17018) to Pair("Ruth Negga", 1),
                createPersonUrn(20561) to Pair("James Gray", 1)
            )
            verifyPeopleList(expectedPeople, peopleChannel)

            sutJob.cancel()
            peopleJob.cancel()
        }
    }
}