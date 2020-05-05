package com.codingventures.movies.ingester.processor

import com.codingventures.movies.domain.Gender
import com.codingventures.movies.domain.Picture
import com.codingventures.movies.ingester.remote.tmdb.response.Images
import com.codingventures.movies.ingester.remote.tmdb.response.Profile
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe

class PersonDetailsMapperTests : ShouldSpec() {
    val imagesResponse = Images(
        profiles = listOf(
            Profile(
                language = null,
                aspectRatio = 1.8,
                voteAverage = 1.4,
                voteCount = 10,
                width = 320,
                height = 640,
                filePath = "/foo.jpg"
            )
        )
    )

    init {
        should("map gender from response") {
            mapGender(1) shouldBe Gender.Female
            mapGender(2) shouldBe Gender.Male
            mapGender(10) shouldBe Gender.Unstated
        }
        should("map profile pictures from response") {
            val expectedPictures = listOf(
                Picture(
                    path = "/foo.jpg",
                    width = 320,
                    height = 640
                )
            )
            val result = mapPictures(imagesResponse)
            result shouldBe expectedPictures
        }
    }
}