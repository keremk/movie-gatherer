package com.codingventures.movies.ingester.reader

import com.codingventures.movies.domain.FetchTask
import com.sksamuel.avro4k.Avro
import org.apache.avro.generic.GenericRecord
import org.apache.kafka.clients.consumer.ConsumerRecord

data class RecordDeserializationException(
    val record: ConsumerRecord<String, GenericRecord>,
    val inner: Exception
): Exception()

fun fetchTaskFromRecord(record: ConsumerRecord<String, GenericRecord>): FetchTask {
    try {
        return Avro.default.fromRecord(FetchTask.serializer(), record.value())
    } catch (e: Exception) {
        throw RecordDeserializationException(record = record, inner = e)
    }
}