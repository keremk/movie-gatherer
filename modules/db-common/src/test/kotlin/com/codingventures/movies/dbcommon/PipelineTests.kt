package com.codingventures.movies.dbcommon

import com.codingventures.movies.mockdata.domain.mockMovieDetails
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import io.vertx.sqlclient.Tuple

class PipelineTests : ShouldSpec() {
    data class MockDbModel(
        val externalUrns: Array<String>
    )

    init {
        should("Convert to tuple") {
            val externalUrns = listOf("cv:movies:12345", "tmdb:movies:531334")
            val title = "Joker"
            val values = listOf(title, externalUrns)

            val tupleValues = Tuple.tuple(values)
//            val expectedTupleValues = Tuple.tuple().addString(title).addStringArray(externalUrns)
            tupleValues shouldBe null
        }
    }
}