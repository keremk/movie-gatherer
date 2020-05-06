package com.codingventures.movies.ingester.processor

import com.codingventures.movies.ingester.remote.tmdb.tasks.personDetailsFetchTask
import com.codingventures.movies.ingester.remote.tmdb.tasks.popularMoviesFetchTask
import com.codingventures.movies.ingester.remote.tmdb.response.Cast as CastResponse
import com.codingventures.movies.ingester.remote.tmdb.response.Crew as CrewResponse
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe

class ResponseProcessorTests : ShouldSpec() {
    val crew = listOf(
        CrewResponse(
            creditId = "55fdcfa7c3a368133400196b",
            department = "Production",
            gender = 2,
            id = 500,
            job = "Producer",
            name = "Tom Cruise",
            profilePath = "/3xuBtKtQwAUnEAoMfYseNSFhRkq.jpg"
        )
    )

    val cast = listOf(
        CastResponse(
            castId = 0,
            character = "Jack Reacher",
            creditId = "5573971c9251413f6600024d",
            gender = 2,
            id = 500,
            name = "Tom Cruise",
            order = 0,
            profilePath = "/3xuBtKtQwAUnEAoMfYseNSFhRkq.jpg"
        )
    )

    init {
        should("prepare next page of movie list fetch task") {
            val expectedTask = popularMoviesFetchTask(2)
            val result = nextPageMovieListTask(1, 5)
            result shouldBe expectedTask
        }
        should("return null task if next page is more than total pages"){
            val result = nextPageMovieListTask(5, 5)
            result shouldBe null
        }
        should("prepare person details fetch tasks") {
            val expectedTasks = listOf(
                personDetailsFetchTask("500"),
                personDetailsFetchTask("500")
            )
            val result = personDetailsTasks(crew, cast)
            result shouldBe expectedTasks
        }
    }
}