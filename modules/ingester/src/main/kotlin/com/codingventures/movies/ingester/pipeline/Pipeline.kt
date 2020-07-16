package com.codingventures.movies.ingester.pipeline

import com.codingventures.movies.domain.MovieIndustryData
import com.codingventures.movies.ingester.processor.ResponseProcessorException
import com.codingventures.movies.ingester.processor.processResponse
import com.codingventures.movies.ingester.producer.Producers
import com.codingventures.movies.ingester.producer.ResultsProductionException
import com.codingventures.movies.ingester.producer.TaskProducer
import com.codingventures.movies.domain.FetchTask
import com.codingventures.movies.ingester.reader.RecordDeserializationException
import com.codingventures.movies.ingester.reader.fetchTaskFromRecord
import com.codingventures.movies.ingester.remote.tmdb.config.RemoteConfigProvider
import com.codingventures.movies.ingester.remote.tmdb.fetchers.FetchOperationException
import com.codingventures.movies.ingester.remote.tmdb.fetchers.RemoteClient
import com.codingventures.movies.ingester.remote.tmdb.fetchers.TmdbClient
import com.codingventures.movies.kafka.KafkaConfigProvider
import com.codingventures.movies.kafka.KafkaRunner
import com.codingventures.movies.kafka.KafkaTopics
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
import org.apache.kafka.clients.consumer.Consumer
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.clients.consumer.ConsumerRecords
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.Producer

private val logger = KotlinLogging.logger {}

data class ProcessingContext(
    val kafkaProducer: Producer<String, GenericRecord>,
    val kafkaConsumer: Consumer<String, GenericRecord>,
    val remoteClient: RemoteClient,
    val topics: KafkaTopics
)


class Pipeline(
    val kafkaRunner: KafkaRunner,
    val ctx: ProcessingContext
) {

    @ExperimentalCoroutinesApi
    fun Flow<ConsumerRecord<String, GenericRecord>>.processTasks(ctx: ProcessingContext, producers: Producers):
            Flow<Pair<MovieIndustryData?, List<FetchTask>>> = this
        .map { fetchTaskFromRecord(it) }
        .map { ctx.remoteClient.fetchData(it) }
        .map { processResponse(it) }
        .onEach { producers.produceDataAndTasks(it, ctx.topics.tasks) }

    @ExperimentalCoroutinesApi
    suspend fun run() = coroutineScope {
        val topicChannel = Channel<ConsumerRecords<String, GenericRecord>>()
        val commitRecordsChannel = Channel<ConsumerRecords<String, GenericRecord>>()
        launch(Dispatchers.IO) {
            kafkaRunner.run(listOf(ctx.topics.tasks), topicChannel, commitRecordsChannel)
        }
        val producers = Producers(ctx.kafkaProducer, ctx.topics)
        while (isActive) {
            val tasks = topicChannel.receive()
            createFlow(tasks)
                .processTasks(ctx, producers)
                .onCompletion {cause ->
                    if (cause != null) logger.error { "Flow completed with an exception" }
                }
                .catch { e ->
                    when (e) {
                        is RecordDeserializationException -> logger.error { "Record ${e.record} is not deserialized - ${e.inner.message}" }
                        is FetchOperationException -> handleFetchOperationException(producers, e)
                        is ResponseProcessorException -> logger.error { "Response ${e.response} can not be processed - ${e.inner.message}" }
                        is ResultsProductionException -> handleResultsProductionException(e)
                        else -> logger.error { "Unhandled exception ${e.message}" }
                    }
                }
                .collect()
            commitRecordsChannel.send(tasks)
        }
        ctx.kafkaProducer.close()
    }

    private suspend fun handleFetchOperationException(producers: Producers, e: FetchOperationException) =
        when (e.inner) {
            is ServerResponseException -> producers.produce(TaskProducer(listOf(e.task), ctx.topics.deadLetters))
            else -> logger.error { "Task ${e.task.endpoint.path} with params ${e.task.endpoint.params} did not succeed - ${e.inner.message}" }
        }

    private fun handleResultsProductionException(e: ResultsProductionException) {
        logger.error { "Unable to post the results to topics - ${e.inner.message}" }
    }

    private fun createFlow(records: ConsumerRecords<String, GenericRecord>) = flow {
        records.forEach { record ->
            logger.info { "Reading new Job with offset = ${record.offset()}, key = ${record.key()}, value = ${record.value()}" }
            emit(record)
        }
    }

    @KtorExperimentalAPI
    companion object {
        fun initialize(
            kafkaConfigProvider: KafkaConfigProvider = KafkaConfigProvider.default(),
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
            return Pipeline(
                kafkaRunner = kafkaRunner,
                ctx = ProcessingContext(
                    kafkaProducer = KafkaProducer<String, GenericRecord>(kafkaConfigProvider.producerProperties()),
                    kafkaConsumer = kafkaRunner.kafkaConsumer,
                    remoteClient = tmdbClient,
                    topics = kafkaConfigProvider.kafkaTopics
                )
            )
        }
    }
}