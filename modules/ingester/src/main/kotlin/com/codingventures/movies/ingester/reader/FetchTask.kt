package com.codingventures.movies.ingester.reader

import com.codingventures.movies.domain.FetchTask
import com.sksamuel.avro4k.Avro
import org.apache.avro.generic.GenericRecord
import org.apache.kafka.clients.consumer.ConsumerRecord

//data class RecordDeserializationException(
//    val record: ConsumerRecord<String, GenericRecord>,
//    val inner: Exception
//): Exception()
//
//fun fetchTaskFromRecord(record: ConsumerRecord<String, GenericRecord>): Pair<FetchTask, ConsumerRecord<String, GenericRecord>> {
//    try {
//        val fetchTask = Avro.default.fromRecord(FetchTask.serializer(), record.value())
//        return Pair(fetchTask, record)
//    } catch (e: Exception) {
//        throw RecordDeserializationException(record = record, inner = e)
//    }
//}