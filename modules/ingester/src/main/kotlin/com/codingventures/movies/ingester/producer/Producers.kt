package com.codingventures.movies.ingester.producer

import com.codingventures.movies.domain.*
import com.codingventures.movies.kafka.KafkaTopics
import com.codingventures.movies.kafka.dispatchRecord
import com.sksamuel.avro4k.Avro
import mu.KotlinLogging
import org.apache.avro.generic.GenericRecord
import org.apache.kafka.clients.producer.Producer
import org.apache.kafka.clients.producer.ProducerRecord
import java.lang.Exception
import java.util.*

private val logger = KotlinLogging.logger {}

data class ResultsProductionException(
    val resultPair: ProductionTask,
    val inner: Exception
) : Exception()

class Producers(
    val kafkaProducer: Producer<String, GenericRecord>,
    val topics: KafkaTopics
) {
    suspend fun produce(input: Producable) = when (input) {
        is MovieIndustryDataProducer -> input.produce(kafkaProducer, topics)
        is MovieDetailsProducer -> input.produce(kafkaProducer, topics)
        is PersonDetailsProducer -> input.produce(kafkaProducer, topics)
        is TaskProducer -> input.produce(kafkaProducer, topics)
    }

    suspend fun produceDataAndTasks(results: ProductionTask, taskTopic: String) {
        try {
            results.movieIndustryData?.also { produce(MovieIndustryDataProducer(it)) }
            produce(TaskProducer(results.additionalTasks, taskTopic))
        } catch (e: Exception) {
            throw ResultsProductionException(results, e)
        }
    }
}

sealed class Producable() {
    abstract suspend fun produce(kafkaProducer: Producer<String, GenericRecord>, topics: KafkaTopics): Unit
}

class MovieIndustryDataProducer(
    val data: MovieIndustryData
) : Producable() {

    override suspend fun produce(kafkaProducer: Producer<String, GenericRecord>, topics: KafkaTopics) =
        when (data) {
            is MovieDetails -> MovieDetailsProducer(data).produce(kafkaProducer, topics)
            is PersonDetails -> PersonDetailsProducer(data).produce(kafkaProducer, topics)
        }
}

class MovieDetailsProducer(
    val movieDetails: MovieDetails
) : Producable() {

    override suspend fun produce(kafkaProducer: Producer<String, GenericRecord>, topics: KafkaTopics): Unit {
        val record = Avro.default.toRecord(MovieDetails.serializer(), movieDetails)
        val key = movieDetails.movieUrn.toString()

        logger.info { "Publishing Movie Entry - ${movieDetails.title} with key: $key" }
        dispatchRecord(kafkaProducer, ProducerRecord<String, GenericRecord>(topics.movies, key, record))
    }
}

class PersonDetailsProducer(
    val personDetails: PersonDetails
) : Producable() {

    override suspend fun produce(kafkaProducer: Producer<String, GenericRecord>, topics: KafkaTopics) {
        val record = Avro.default.toRecord(PersonDetails.serializer(), personDetails)
        val key = personDetails.personUrn.toString()

        logger.info { "Publishing Person Entry - ${personDetails.name} with key: $key" }
        dispatchRecord(kafkaProducer, ProducerRecord<String, GenericRecord>(topics.people, key, record))
    }
}

class TaskProducer(
    val tasks: List<FetchTask>,
    val topic: String
) : Producable() {

    override suspend fun produce(kafkaProducer: Producer<String, GenericRecord>, topics: KafkaTopics) {
        tasks.forEach { task ->
            val jobRecord = Avro.default.toRecord(FetchTask.serializer(), task)
            val key = UUID.randomUUID().toString()

            logger.info { "Publishing New Task - ${task.taskType} with key: $key" }
            dispatchRecord(kafkaProducer, ProducerRecord<String, GenericRecord>(topic, key, jobRecord))
        }
    }
}


