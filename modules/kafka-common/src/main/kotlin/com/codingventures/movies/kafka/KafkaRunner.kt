package com.codingventures.movies.kafka

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import mu.KotlinLogging
import org.apache.avro.generic.GenericRecord
import org.apache.kafka.clients.consumer.*
import org.apache.kafka.common.TopicPartition
import java.lang.IllegalArgumentException
import java.time.Duration

private val logger = KotlinLogging.logger {}

class KafkaRunner(
    val kafkaConsumer: Consumer<String, GenericRecord>,
    val autoCommitEnabled: Boolean
) {
    suspend fun run(topics: List<String>,
                    recordsConsumed: Channel<ConsumerRecords<String, GenericRecord>>,
                    recordsToCommit: Channel<ConsumerRecords<String, GenericRecord>>? = null) = coroutineScope {
        try {
            if (!autoCommitEnabled && recordsToCommit == null) {
                throw IllegalArgumentException("If autocommit is disabled, recordsToCommit cannot be null, please specify a valid channel")
            }
            kafkaConsumer.subscribe(topics)
            logger.debug { "Listening to topics $topics" }
            while (isActive) {
                val records = kafkaConsumer.poll(Duration.ofMillis(1000))
                if (!records.isEmpty) {
                    recordsConsumed.send(records)
                }
                if (!autoCommitEnabled) {
                    val commitRecords = recordsToCommit?.receive()
                    if (commitRecords != null) kafkaConsumer.commitSync(calculateNextOffsets(commitRecords))
                }
            }
        } finally {
            kafkaConsumer.close()
        }
    }

    private fun calculateNextOffsets(records: ConsumerRecords<String, GenericRecord>):  Map<TopicPartition, OffsetAndMetadata> {
        return records.partitions().fold(mutableMapOf()) { partitionToOffsets, partition ->
            val partitionRecords = records.records(partition)
            val lastOffset = partitionRecords.last().offset()
            partitionToOffsets.apply { put(partition, OffsetAndMetadata(lastOffset + 1)) }
        }
    }

    companion object {
        fun initialize(kafkaConfigProvider: KafkaConfigProvider): KafkaRunner {
            val properties = kafkaConfigProvider.consumerProperties()
            val autoCommitFlag = properties[ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG]
            val autoCommitEnabled = if (autoCommitFlag != null) autoCommitFlag as Boolean else true
            return KafkaRunner(
                kafkaConsumer = KafkaConsumer<String, GenericRecord>(properties),
                autoCommitEnabled = autoCommitEnabled
            )
        }
    }
}