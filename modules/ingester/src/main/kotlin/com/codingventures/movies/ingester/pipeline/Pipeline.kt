package com.codingventures.movies.ingester.pipeline

import com.codingventures.movies.ingester.processor.ResponseProcessorException
import com.codingventures.movies.ingester.producer.Producers
import com.codingventures.movies.ingester.producer.ResultsProductionException
import com.codingventures.movies.ingester.producer.TaskProducer
import com.codingventures.movies.domain.FetchTask
import com.codingventures.movies.domain.ProductionTask
import com.codingventures.movies.ingester.config.KafkaTopicsProvider
import com.codingventures.movies.ingester.processor.ResponseProcessor
import com.codingventures.movies.ingester.remote.tmdb.config.RemoteConfigProvider
import com.codingventures.movies.ingester.remote.tmdb.fetchers.FetchOperationException
import com.codingventures.movies.ingester.remote.tmdb.fetchers.RemoteClient
import com.codingventures.movies.ingester.remote.tmdb.fetchers.TmdbClient
import com.codingventures.movies.ingester.remote.tmdb.response.TmdbResponse
import com.codingventures.movies.kafka.KafkaConfigProvider
import com.codingventures.movies.kafka.KafkaRunner
import com.github.avrokotlin.avro4k.Avro
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.features.ServerResponseException
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.util.KtorExperimentalAPI
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import mu.KotlinLogging
import org.apache.avro.generic.GenericRecord
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.clients.consumer.ConsumerRecords
import org.apache.kafka.clients.consumer.OffsetAndMetadata
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.common.TopicPartition

private val logger = KotlinLogging.logger {}

enum class Reason {
    FetchTaskInvalidFailure,
    DataRetrievalFailure,
    ResponseProcessingFailure,
    PublishStageFailure
}

data class PipelineStageException(
    val reason: Reason,
    val record: ConsumerRecord<String, GenericRecord>,
    val inner: Exception
): Exception()

typealias InputRecord = ConsumerRecord<String, GenericRecord>

class Pipeline(
    val kafkaRunner: KafkaRunner,
    val producers: Producers,
    val remoteClient: RemoteClient,
    val topics: KafkaTopicsProvider,
    val responseProcessor: ResponseProcessor
) {

    @ExperimentalCoroutinesApi
    fun Flow<InputRecord>.processTasks(producers: Producers):
            Flow<Pair<ProductionTask, InputRecord>> = this
        .map { fetchTaskFromRecord(it) }
        .map { fetchData(it) }
        .map { processResponse(it) }
        .onEach { produceDataAndTasks(producers, it) }

    private fun fetchTaskFromRecord(record: InputRecord): Pair<FetchTask, InputRecord> {
        try {
            val fetchTask = Avro.default.fromRecord(FetchTask.serializer(), record.value())
            return Pair(fetchTask, record)
        } catch (e: Exception) {
            throw PipelineStageException(Reason.FetchTaskInvalidFailure, record, e)
        }
    }

    private suspend fun fetchData(input: Pair<FetchTask, InputRecord>): Pair<TmdbResponse, InputRecord> {
        try {
            val data = remoteClient.fetchData(input.first)
            return Pair(data, input.second)
        } catch (e: FetchOperationException) {
            throw PipelineStageException(Reason.DataRetrievalFailure, input.second, e)
        }
    }

    private fun processResponse(input: Pair<TmdbResponse, InputRecord>): Pair<ProductionTask, InputRecord> {
        try {
            return Pair(responseProcessor.process(input.first), input.second)
        } catch (e: Exception) {
            throw PipelineStageException(Reason.ResponseProcessingFailure, input.second, e)
        }
    }

    private suspend fun produceDataAndTasks(producers: Producers, input: Pair<ProductionTask, InputRecord>) {
        try {
            producers.produceDataAndTasks(input.first, topics.tasks)
        } catch (e: Exception) {
            throw PipelineStageException(Reason.PublishStageFailure, input.second, e)
        }
    }

    @ExperimentalCoroutinesApi
    suspend fun run() = coroutineScope {
        val topicChannel = Channel<ConsumerRecords<String, GenericRecord>>()
        val commitRecordsChannel = Channel<Map<TopicPartition, OffsetAndMetadata>>()
        launch(Dispatchers.IO) {
            kafkaRunner.run(listOf(topics.tasks), topicChannel, commitRecordsChannel)
        }
        while (isActive) {
            val records = topicChannel.receive()
            val offsets: Map<TopicPartition, OffsetAndMetadata> = records.partitions().fold(mutableMapOf()) { partitionToOffsets, partition ->
                val recordsInPartition = records.records(partition)

                createFlow(recordsInPartition)
                    .processTasks(producers)
                    .onCompletion { cause ->
                        if (cause == null) {
                            val nextOffset = recordsInPartition.last().offset() + 1
                            partitionToOffsets.apply { put(partition, OffsetAndMetadata(nextOffset)) }
                            logger.info { "All completed, lastOffset = $nextOffset" }
                        }
                    }
                    .catch { e ->
                        if (e !is PipelineStageException) {
                            throw UnknownError("Unexpected exception ${e.message}, shutting down...")
                        }

                        when (e.reason) {
                            Reason.FetchTaskInvalidFailure -> handleFetchTaskInvalidFailure(e)
                            Reason.DataRetrievalFailure -> handleFetchOperationException(producers, e.inner)
                            Reason.ResponseProcessingFailure -> handleResponseProcessingException(e.inner)
                            Reason.PublishStageFailure -> handleResultsProductionException(e.inner)
                        }
                        val nextOffset = e.record.offset() + 1
                        partitionToOffsets.apply { put(partition, OffsetAndMetadata(nextOffset)) }
                        logger.info { "Error occured, lastOffset = $nextOffset" }
                    }
                    .collect()
                partitionToOffsets
            }
            logger.info { "Verify offsets ${offsets.entries}"}
            commitRecordsChannel.send(offsets)
        }
        producers.close()
    }

    private fun handleFetchTaskInvalidFailure(e: PipelineStageException) {
        logger.error { "Task definition ${e.record} is not valid, skipping task: ${e.inner.message}" }
    }

    private suspend fun handleFetchOperationException(producers: Producers, e: Exception) {
        if (e !is FetchOperationException) {
            throw UnknownError("Unexpected exception ${e.message}, shutting down...")
        }

        when (e.inner) {
            is ServerResponseException -> producers.produce(TaskProducer(listOf(e.task), topics.deadLetters))
            else -> logger.error { "Task ${e.task.endpoint.path} with params ${e.task.endpoint.params} did not succeed - ${e.inner.message}" }
        }
    }

    private fun handleResponseProcessingException(e: Exception) {
        if (e !is ResponseProcessorException) {
            throw UnknownError("Unexpected exception ${e.message}, shutting down...")
        }

        logger.error { "Response ${e.response} can not be processed - ${e.inner.message}" }
    }

    private fun handleResultsProductionException(e: Exception) {
        if (e !is ResultsProductionException) {
            throw UnknownError("Unexpected exception ${e.message}, shutting down...")
        }

        logger.error { "Unable to post the results to topics - ${e.inner.message}" }
    }

    private fun createFlow(records: List<ConsumerRecord<String, GenericRecord>>) = flow {
        records.forEach { record ->
            logger.info { "Reading new Job with offset = ${record.offset()}, key = ${record.key()}, value = ${record.value()}" }
            emit(record)
        }
    }

    @KtorExperimentalAPI
    companion object {
        fun initialize(
            kafkaConfigProvider: KafkaConfigProvider = KafkaConfigProvider.default(),
            kafkaTopicsProvider: KafkaTopicsProvider = KafkaTopicsProvider.default(),
            remoteConfigProvider: RemoteConfigProvider = RemoteConfigProvider.default()
        ): Pipeline {
            val httpClient = HttpClient(CIO) {
                install(JsonFeature) {
                    serializer = KotlinxSerializer(
                        Json(JsonConfiguration.Stable.copy(isLenient = true, ignoreUnknownKeys = true))
                    )
                }
            }
            val tmdbClient = TmdbClient(httpClient, remoteConfigProvider)
            val kafkaRunner = KafkaRunner.initialize(kafkaConfigProvider)
            val kafkaProducer = KafkaProducer<String, GenericRecord>(kafkaConfigProvider.producerProperties())
            val producers = Producers(kafkaProducer, kafkaTopicsProvider)
            val responseProcessor = ResponseProcessor.initialize(
                maxNoCrewRequests = remoteConfigProvider.maxNoCrewRequests,
                maxNoCastRequests = remoteConfigProvider.maxNoCastRequests,
                maxNoPages = remoteConfigProvider.maxNoPages
            )
            printInitializedMessage(kafkaConfigProvider, kafkaTopicsProvider, remoteConfigProvider)
            return Pipeline(
                kafkaRunner = kafkaRunner,
                producers = producers,
                remoteClient = tmdbClient,
                topics = kafkaTopicsProvider,
                responseProcessor = responseProcessor
            )
        }

        private fun printInitializedMessage(
            kafkaConfigProvider: KafkaConfigProvider,
            kafkaTopicsProvider: KafkaTopicsProvider,
            remoteConfigProvider: RemoteConfigProvider
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
                Movies Topic: ${kafkaTopicsProvider.movies} 
                People Topic: ${kafkaTopicsProvider.people} 
                Tasks Topic: ${kafkaTopicsProvider.tasks} 
                Deadletters Topic: ${kafkaTopicsProvider.deadLetters} 
                TMDB Configuration: 
                ------------------- 
                Hostname: ${remoteConfigProvider.host} 
                Port: ${remoteConfigProvider.port} 
                HTTPS Enabled: ${remoteConfigProvider.https} 
                Max # Pages: ${remoteConfigProvider.maxNoPages} 
                Max # Crew Requests: ${remoteConfigProvider.maxNoCrewRequests} 
                Max # Cast Requests: ${remoteConfigProvider.maxNoCastRequests} 
            """.trimIndent()
        }
    }
}