package com.codingventures.movies.ingester.testutils

import io.confluent.kafka.serializers.KafkaAvroSerializer
import org.apache.avro.generic.GenericRecord
import org.apache.avro.reflect.MapEntry
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.clients.consumer.MockConsumer
import org.apache.kafka.clients.consumer.OffsetResetStrategy
import org.apache.kafka.common.TopicPartition
import org.apache.kafka.common.serialization.Serializer

class MockKafkaAvroGenericRecordSerializer: Serializer<GenericRecord> {
    override fun serialize(topic: String?, data: GenericRecord?): ByteArray {
        return KafkaAvroSerializer().serialize(topic, data)
    }
}

fun mockConsumer(records: Map<String, List<ConsumerRecord<String, GenericRecord>>>): MockConsumer<String, GenericRecord> {
    val topicCollection = records.toList().map { it.first }
    val partitions = records.toList().map { TopicPartition(it.first, 0)}
    val partitionsBeginningMap = partitions.associateBy( { it }, { 0L } )
    val partitionsEndMap = records.toList().associateBy(
        { TopicPartition(it.first, 0) },
        { it.second.count().toLong() })
    val allRecords = records.toList().map { it.second }.flatten()

    return MockConsumer<String, GenericRecord>(OffsetResetStrategy.EARLIEST).apply {
        subscribe(topicCollection)
        rebalance(partitions)
        updateBeginningOffsets(partitionsBeginningMap)
        updateEndOffsets(partitionsEndMap)
        allRecords.forEach{ addRecord(it) }
    }
}