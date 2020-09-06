package com.codingventures.movies.peopledb.tests.integration

import com.codingventures.movies.containers.TestKafkaSystem
import com.codingventures.movies.dbcommon.config.PgConfigProvider
import com.codingventures.movies.dbcommon.pipeline.Pipeline
import com.codingventures.movies.dbcommon.utils.connectToDb
import com.codingventures.movies.domain.PersonDetails
import com.codingventures.movies.kafka.ConsumerSettings
import com.codingventures.movies.kafka.KafkaConfigProvider
import com.codingventures.movies.kafka.KafkaTopics
import com.codingventures.movies.kafka.dispatchRecord
import com.codingventures.movies.mockdata.domain.mockPersonDetailsList
import com.codingventures.movies.peopledb.mapper.prepareStatements
import com.codingventures.movies.peopledb.tests.utils.initializeTargetDb
import com.codingventures.movies.utils.equalTo
import com.codingventures.movies.utils.retrySuspending
import com.codingventures.movies.utils.until
import com.sksamuel.avro4k.Avro
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.Spec
import io.kotest.core.spec.style.ShouldSpec
import io.vertx.kotlin.sqlclient.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import mu.KotlinLogging
import org.apache.avro.generic.GenericRecord
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerRecord

private val logger = KotlinLogging.logger {}

@ExperimentalCoroutinesApi
class EndToEndTests : ShouldSpec() {
    private lateinit var sutRunner: Pipeline<PersonDetails>
    private lateinit var producer: KafkaProducer<String, GenericRecord>
    private lateinit var pgConfigProvider: PgConfigProvider

    private val kafkaTopics = KafkaTopics(
        movies = "movies",
        people = "people",
        deadLetters = "deadletters",
        tasks = "tasks"
    )

    override fun isolationMode(): IsolationMode? = IsolationMode.InstancePerTest

    override fun beforeSpec(spec: Spec) {
        super.beforeSpec(spec)
        pgConfigProvider = initializeTargetDb()
        initializeKafkaSystem()
    }

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
            pgConfigProvider = pgConfigProvider,
            consumerTopic = kafkaTopics.people,
            deserializer = PersonDetails.serializer(),
            prepareStatements = ::prepareStatements
        )

        producer = KafkaProducer<String, GenericRecord>(sutKafkaConfigProvider.producerProperties())

    }

    private suspend fun produce(personDetails: PersonDetails) {
        val record = Avro.default.toRecord(PersonDetails.serializer(), personDetails)
        val key = personDetails.personUrn.toString()

        dispatchRecord(producer, ProducerRecord<String, GenericRecord>(kafkaTopics.people, key, record))
    }

    private fun produceMockPersonData() = runBlocking {
        mockPersonDetailsList.forEach {
            produce(it)
        }
    }

    init {
        should("Persist movies to DB after reading from Kafka") {
            produceMockPersonData()

            val sutJob = launch {
                sutRunner.run()
            }
            val client = connectToDb(pgConfigProvider)

            retrySuspending(10, 100000, 2.0) {
                val rows = client.query("SELECT * FROM people").executeAwait()
                logger.info { "Trying to get person, current row count is ${rows.rowCount()}"}
                rows.rowCount()
            }.until(equalTo(4))

            client.close()
            sutJob.cancel()
        }
    }
}