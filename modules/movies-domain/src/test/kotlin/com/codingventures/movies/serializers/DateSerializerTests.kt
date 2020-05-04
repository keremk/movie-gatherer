package com.codingventures.movies.serializers

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.startWith
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

@kotlinx.serialization.UnstableDefault
class DateSerializerTests : ShouldSpec() {
    @Serializable
    data class Subject(
        @Serializable(with = DateSerializer::class) val testedDate: LocalDate
    )

    private val json = Json(JsonConfiguration.Default)

    init {
        should("serialize Date to string with yyyy-MM-dd format") {
            val expectedValue = """{"testedDate":"2010-05-10"}"""
            val inputObj = Subject(testedDate = LocalDate.parse("2010-05-10", DateTimeFormatter.ISO_LOCAL_DATE))
            val result = json.stringify(Subject.serializer(), inputObj)

            result shouldBe expectedValue
        }
        should("deserialize string to Date with yyyy-MM-dd format") {
            val expectedValue = LocalDate.parse("2010-05-10", DateTimeFormatter.ISO_LOCAL_DATE)
            val inputString = """{"testedDate":"2010-05-10"}"""
            val result = json.parse(Subject.serializer(), inputString)

            result.testedDate shouldBe expectedValue
        }
        should("raise an exception if the incoming string is not formatted expectedly") {
            val exception = shouldThrow<DateTimeParseException> {
                json.parse(Subject.serializer(), """{"testedDate":"20-10-1002"}""")
            }
            exception.message should startWith("Text '20-10-1002'")
        }
    }
}