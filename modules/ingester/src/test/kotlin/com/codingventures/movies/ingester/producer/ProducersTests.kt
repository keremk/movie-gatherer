package com.codingventures.movies.ingester.producer

import com.codingventures.movies.domain.MovieDetails
import com.codingventures.movies.domain.FetchTask
import com.codingventures.movies.domain.ProductionTask
import com.codingventures.movies.ingester.remote.tmdb.tasks.personDetailsFetchTask
import com.codingventures.movies.ingester.testutils.MockKafkaAvroGenericRecordSerializer
import com.codingventures.movies.kafka.KafkaTopics
import com.codingventures.movies.mockdata.domain.mockMovieDetails
import com.sksamuel.avro4k.Avro
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.shouldBe
import org.apache.kafka.clients.producer.MockProducer
import org.apache.kafka.common.serialization.StringSerializer

class ProducersTests : ShouldSpec() {
    fun isValidUUID(uuid: String): Boolean {
        val regex = Regex("^[0-9a-f]{8}-[0-9a-f]{4}-[0-5][0-9a-f]{3}-[089ab][0-9a-f]{3}-[0-9a-f]{12}", RegexOption.IGNORE_CASE)
        return regex.containsMatchIn(uuid)
    }

    init {
        should("only produce new fetch tasks when there is no new data") {
            val producers = Producers(
                kafkaProducer = MockProducer(true, StringSerializer(), MockKafkaAvroGenericRecordSerializer()),
                topics = KafkaTopics(movies = "testmovies", people = "testpeople", tasks = "testtasks", deadLetters = "testLetters")
            )
            val input = ProductionTask(
                movieIndustryData = null,
                additionalTasks = listOf(
                    personDetailsFetchTask("1234")
                )
            )
            producers.produceDataAndTasks(input, producers.topics.tasks)
            val history = (producers.kafkaProducer as MockProducer).history()
            val expectedRecord = Avro.default.toRecord(FetchTask.serializer(), input.additionalTasks[0])
            history.count() shouldBe 1
            history[0].value() shouldBe expectedRecord
            isValidUUID(history[0].key()) shouldBe true
        }

        should("produce both movie industry data and fetch tasks when there is both") {
            val producers = Producers(
                kafkaProducer = MockProducer(true, StringSerializer(), MockKafkaAvroGenericRecordSerializer()),
                topics = KafkaTopics(movies = "testmovies", people = "testpeople", tasks = "testtasks", deadLetters = "testLetters")
            )
            val input = ProductionTask(
                movieIndustryData = mockMovieDetails,
                additionalTasks = listOf(
                    personDetailsFetchTask("1234")
                )
            )
            producers.produceDataAndTasks(input, producers.topics.tasks)
            val history = (producers.kafkaProducer as MockProducer).history()

            val expectedMovieDetails = input.movieIndustryData as MovieDetails
            val expectedMovieRecord = Avro.default.toRecord(MovieDetails.serializer(), expectedMovieDetails)
            val expectedFetchTaskRecord = Avro.default.toRecord(FetchTask.serializer(), input.additionalTasks[0])

            history.count() shouldBe 2
            val results = listOf(history[0].value(), history[1].value())
            results.shouldContainExactlyInAnyOrder(expectedMovieRecord, expectedFetchTaskRecord)
        }
    }
}