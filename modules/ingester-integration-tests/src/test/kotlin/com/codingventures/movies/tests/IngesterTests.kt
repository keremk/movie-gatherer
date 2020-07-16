package com.codingventures.movies.tests

import com.codingventures.movies.containers.MockTmdbServer
import com.codingventures.movies.containers.TestKafkaSystem
import com.codingventures.movies.domain.*
import com.codingventures.movies.ingester.pipeline.Pipeline
import com.codingventures.movies.ingester.producer.Producers
import com.codingventures.movies.ingester.producer.TaskProducer
import com.codingventures.movies.ingester.remote.tmdb.config.RemoteConfigProvider
import io.kotest.core.spec.Spec
import io.kotest.core.spec.style.ShouldSpec
import com.codingventures.movies.ingester.remote.tmdb.fetchers.TmdbClient
import com.codingventures.movies.ingester.remote.tmdb.tasks.personDetailsFetchTask
import com.codingventures.movies.ingester.remote.tmdb.tasks.popularMoviesFetchTask
import com.codingventures.movies.kafka.ConsumerSettings
import com.codingventures.movies.kafka.KafkaConfigProvider
import com.codingventures.movies.kafka.KafkaRunner
import com.codingventures.movies.kafka.KafkaTopics
import com.sksamuel.avro4k.Avro
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.util.KtorExperimentalAPI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
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

    private val kafkaTopics = KafkaTopics(
        movies = "movies",
        people = "people",
        deadLetters = "deadletters",
        tasks = "tasks"
    )

    @KtorExperimentalAPI
    override fun beforeSpec(spec: Spec) {
        super.beforeSpec(spec)
        initalizeMockTmdbServer()
        initializeKafkaSystem()
    }

    override fun afterSpec(spec: Spec) {
        super.afterSpec(spec)
//        cleanupKafkaSystem()
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
        val serverConfig = TestKafkaSystem.initialize()

        val sutKafkaConfigProvider = KafkaConfigProvider(
            serverConfig = serverConfig,
            consumerSettings = ConsumerSettings(
                enableAutoCommit = false,
                maxPollRecords = 10,
                consumerGroupId = "sut-kafka-consumer"
            ),
            kafkaTopics = kafkaTopics
        )
        sutRunner = Pipeline.initialize(
            kafkaConfigProvider = sutKafkaConfigProvider,
            remoteConfigProvider = remoteConfigProvider
        )

        val kafkaProducer = KafkaProducer<String, GenericRecord>(sutKafkaConfigProvider.producerProperties())
        producers = Producers(kafkaProducer,kafkaTopics)

        val moviesListenerConfigProvider = KafkaConfigProvider(
            serverConfig = serverConfig,
            consumerSettings = ConsumerSettings(
                enableAutoCommit = true,
                maxPollRecords = 10,
                consumerGroupId = "movies-listener"
            ),
            kafkaTopics = kafkaTopics
        )
        moviesListener = KafkaRunner.initialize(moviesListenerConfigProvider)

        val peopleListenerConfigProvider = KafkaConfigProvider(
            serverConfig = serverConfig,
            consumerSettings = ConsumerSettings(
                enableAutoCommit = true,
                maxPollRecords = 10,
                consumerGroupId = "people-listener"
            ),
            kafkaTopics = kafkaTopics
        )
        peopleListener = KafkaRunner.initialize(peopleListenerConfigProvider)
    }

    private fun cleanupKafkaSystem() {
        val topics = listOf(kafkaTopics.tasks, kafkaTopics.deadLetters, kafkaTopics.movies, kafkaTopics.people)

        val exitCode = TestKafkaSystem.deleteTopics(topics)
        exitCode shouldBe 1
    }

    init {
        should("Call the mock server") {
            val fetchTask = personDetailsFetchTask("287")
            val tmdbClient = createTmdbClient(remoteConfigProvider)
            val response = tmdbClient.fetchData(fetchTask)

            response shouldNotBe null
        }

        should("Publish movies and people to Kafka topics") {
            val fetchTask = popularMoviesFetchTask(1)
            producers.produce(TaskProducer(listOf(fetchTask), kafkaTopics.tasks))

            val sutJob = launch {
                sutRunner.run()
            }
            val moviesChannel = Channel<ConsumerRecords<String, GenericRecord>>()
            val moviesJob = launch(Dispatchers.IO) {
                moviesListener.run(listOf(kafkaTopics.movies), moviesChannel)
            }
            val peopleChannel = Channel<ConsumerRecords<String, GenericRecord>>()
            val peopleJob = launch(Dispatchers.IO) {
                peopleListener.run(listOf(kafkaTopics.people), peopleChannel)
            }

            val expectedMovies = listOf(
                Pair(createMovieUrn(419704), "Ad Astra"),
                Pair(createMovieUrn(475557), "Joker"),
                Pair(createMovieUrn(496243), "Parasite"))
            var i = 0
            while (i < 3) {
                val records = moviesChannel.receive()

                records.forEach {
                    val movie = Avro.default.fromRecord(MovieDetails.serializer(), it.value())
                    expectedMovies.contains(Pair(movie.movieUrn, movie.title)) shouldBe true
                    i += 1
                }
            }

            val expectedPeople = listOf(
                Pair(createPersonUrn(287), "Brad Pitt"),
                Pair(createPersonUrn(2176), "Tommy Lee Jones"),
                Pair(createPersonUrn(17018), "Ruth Negga"),
                Pair(createPersonUrn(20561),"James Gray"),
                Pair(createPersonUrn(73421), "Joaquin Phoenix"),
                Pair(createPersonUrn(1545693), "Zazie Beetz"),
                Pair(createPersonUrn(57130), "Todd Phillips"),
                Pair(createPersonUrn(20738), "Song Kang-ho"),
                Pair(createPersonUrn(21684), "Bong Joon-ho")
            )
            i = 0
            while (i < 9) {
                val records = peopleChannel.receive()
                records.forEach {
                    val person = Avro.default.fromRecord(PersonDetails.serializer(), it.value())
                    expectedPeople.contains(Pair(person.personUrn, person.name)) shouldBe true
                    i += 1
                }
            }

            sutJob.cancel()
            moviesJob.cancel()
            peopleJob.cancel()
        }
    }
}