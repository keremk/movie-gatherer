package com.codingventures.movies.serializers

import com.codingventures.movies.domain.MalformedUrnException
import com.codingventures.movies.domain.Urn
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.startWith
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import java.time.format.DateTimeParseException
import kotlin.math.exp

@kotlinx.serialization.UnstableDefault
class UrnSerializerTests : ShouldSpec() {
    @Serializable
    data class Subject(
        @Serializable(with = UrnSerializer::class) val testedUrn: Urn
    )

    private val json = Json(JsonConfiguration.Default)

    init {
        should("serialize Urn") {
            val expectedValue = """{"testedUrn":"cv:movies:1234"}"""
            val inputObj = Subject(testedUrn = Urn("cv", "movies", "1234"))
            val result = json.stringify(Subject.serializer(), inputObj)

            result shouldBe expectedValue
        }
        should("deserialize Urn") {
            val expectedValue = Urn("cv", "movies", "1234")
            val inputString = """{"testedUrn":"cv:movies:1234"}"""
            val result = json.parse(Subject.serializer(), inputString)

            result.testedUrn shouldBe expectedValue
        }
        should("raise an exception if the incoming string has less than 3 fields") {
            val exception = shouldThrow<MalformedUrnException> {
                json.parse(Subject.serializer(), """{"testedUrn":"20:2"}""")
            }

            exception.message should startWith("Text '20:2'")
        }
        should("raise an exception if the incoming string has empty fields") {
            val exception = shouldThrow<MalformedUrnException> {
                json.parse(Subject.serializer(), """{"testedUrn":"cv::1234"}""")
            }

            exception.message should startWith("Text 'cv::1234' is missing")
        }
    }
}