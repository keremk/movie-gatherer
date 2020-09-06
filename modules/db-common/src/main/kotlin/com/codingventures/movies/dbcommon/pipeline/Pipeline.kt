package com.codingventures.movies.dbcommon.pipeline

import com.codingventures.movies.dbcommon.config.PgConfigProvider
import com.codingventures.movies.dbcommon.db.DBService
import com.codingventures.movies.dbcommon.utils.bufferTimeout
import com.codingventures.movies.domain.MovieIndustryData
import com.codingventures.movies.kafka.KafkaConfigProvider
import com.codingventures.movies.kafka.KafkaRunner
import com.codingventures.movies.kafka.KafkaTopics
import com.sksamuel.avro4k.Avro
import io.vertx.kotlin.pgclient.PgConnectOptions
import io.vertx.kotlin.pgclient.pgConnectOptionsOf
import io.vertx.kotlin.sqlclient.*
import io.vertx.pgclient.PgException
import io.vertx.pgclient.PgPool
import io.vertx.pgclient.SslMode
import io.vertx.sqlclient.Tuple
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.serialization.DeserializationStrategy
import mu.KotlinLogging
import org.apache.avro.generic.GenericRecord
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.clients.consumer.ConsumerRecords
import org.apache.kafka.clients.consumer.OffsetAndMetadata
import org.apache.kafka.common.TopicPartition
import java.time.Duration

private val logger = KotlinLogging.logger {}

data class StatementDeclaration(
    val insertStatement: String,
    val batchData: List<List<Any>>
)

enum class Reason {
    IncomingDataInvalidFailure,
    PersistenceFailure
}

typealias InputRecord = ConsumerRecord<String, GenericRecord>

data class PipelineStageException(
    val reason: Reason,
    val record: List<InputRecord>,
    val inner: Exception
): Exception()

class Pipeline<T : MovieIndustryData>(
    val kafkaRunner: KafkaRunner,
    val pgClient: PgPool,
    val consumerTopic: String,
    val deserializer: DeserializationStrategy<T>,
    val prepareStatements: (List<T>) -> List<StatementDeclaration>
) {
    @ExperimentalCoroutinesApi
    @ObsoleteCoroutinesApi
    suspend fun run() = coroutineScope {
        val topicChannel = Channel<ConsumerRecords<String, GenericRecord>>()
        val commitRecordsChannel = Channel<Map<TopicPartition, OffsetAndMetadata>>()
        launch(Dispatchers.IO) {
            kafkaRunner.run(listOf(consumerTopic), topicChannel, commitRecordsChannel)
        }
        while (isActive) {
            val records = topicChannel.receive()

            createFlow(records)
                .processMovieIndustryData()
                .catch { e ->
                    if (e !is PipelineStageException) {
                        throw UnknownError("Unexpected exception ${e.message}, shutting down...")
                    }
                    when (e.reason) {
                        Reason.IncomingDataInvalidFailure -> handleIncomingDataInvalidFailure(e)
                        Reason.PersistenceFailure -> handlePersistenceFailure(e)
                    }
                }
                .collect()
        }
    }

    @ObsoleteCoroutinesApi
    @ExperimentalCoroutinesApi
    private fun Flow<InputRecord>.processMovieIndustryData():
            Flow<Pair<List<StatementDeclaration>, List<InputRecord>>> = this
        .bufferTimeout(size = 100, duration = Duration.ofMillis(2000))
        .map { Pair(fetchMovieIndustryDataFromRecord(it), it) }
        .map { Pair(prepareStatements(it.first), it.second) }
        .onEach { persistToDatabase(it) }

    private fun fetchMovieIndustryDataFromRecord(records: List<InputRecord>): List<T> {
        try {
            return records.map { Avro.default.fromRecord(deserializer, it.value()) }
        } catch (e: Exception) {
            throw PipelineStageException(Reason.IncomingDataInvalidFailure, records, e)
        }
    }

    private suspend fun persistToDatabase(input: Pair<List<StatementDeclaration>, List<InputRecord>>) = coroutineScope {
        val dbService = DBService.initialize(pgClient)

        try {
            dbService.insert(input.first)
        } catch (e: PgException) {
            throw PipelineStageException(Reason.PersistenceFailure, input.second, e)
        }
    }

    private fun createFlow(records: ConsumerRecords<String, GenericRecord>) = flow {
        records.forEach { record ->
            logger.info { "Reading new Job with offset = ${record.offset()}, key = ${record.key()}, value = ${record.value()}" }
            emit(record)
        }
    }

    private fun handleIncomingDataInvalidFailure(e: PipelineStageException) {
        logger.error { "Task definition ${e.record} is not valid, skipping task: ${e.inner.message}" }
    }

    private fun handlePersistenceFailure(e: PipelineStageException) {
        logger.error { "Persistence of ${e.record} is not valid, skipping task: ${e.inner.message}" }
    }

    companion object {
        fun <T: MovieIndustryData> initialize(
            kafkaConfigProvider: KafkaConfigProvider = KafkaConfigProvider.default(),
            pgConfigProvider: PgConfigProvider = PgConfigProvider.default(),
            consumerTopic: String,
            deserializer: DeserializationStrategy<T>,
            prepareStatements: (List<T>) -> List<StatementDeclaration>
        ): Pipeline<T> {
            require(consumerTopic == kafkaConfigProvider.kafkaTopics.movies ||
                    consumerTopic == kafkaConfigProvider.kafkaTopics.people
            )
            val kafkaRunner = KafkaRunner.initialize(kafkaConfigProvider)
            val pgClient = initializeDB(pgConfigProvider)

            return Pipeline<T>(
                kafkaRunner = kafkaRunner,
                pgClient = pgClient,
                consumerTopic = consumerTopic,
                deserializer = deserializer,
                prepareStatements = prepareStatements
            )
        }

        private fun initializeDB(pgConfigProvider: PgConfigProvider): PgPool {
            val connectOptions = pgConnectOptionsOf(
                port = pgConfigProvider.port,
                host = pgConfigProvider.host,
                database = pgConfigProvider.database,
                user = pgConfigProvider.user,
                password = pgConfigProvider.password,
                sslMode = SslMode.DISABLE   // TODO: Later when needed to access using SSL
            )

            val poolOptions = poolOptionsOf(
                maxSize = pgConfigProvider.maxPoolSize
            )

            return PgPool.pool(connectOptions, poolOptions)
        }

        private fun printInitializedMessage(
            kafkaConfigProvider: KafkaConfigProvider
        ) = logger.info {  """
                Ingester pipeline initialized with:
                Kafka Configuration: 
                -------------------- 
                Bootstrap Servers: ${kafkaConfigProvider.serverConfig.bootstrapServers} 
                Schema Registry URL: ${kafkaConfigProvider.serverConfig.schemaRegistryUrl} 
                Consumer ID: ${kafkaConfigProvider.consumerSettings.consumerGroupId} 
                Max Poll Records: ${kafkaConfigProvider.consumerSettings.maxPollRecords} 
                Enable Auto Commit: ${kafkaConfigProvider.consumerSettings.enableAutoCommit} 
                Topics: 
                ------- 
                Movies Topic: ${kafkaConfigProvider.kafkaTopics.movies} 
                People Topic: ${kafkaConfigProvider.kafkaTopics.people} 
                Tasks Topic: ${kafkaConfigProvider.kafkaTopics.tasks} 
                Deadletters Topic: ${kafkaConfigProvider.kafkaTopics.deadLetters} 
            """.trimIndent()
        }
    }
}