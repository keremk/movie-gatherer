package com.codingventures.movies.ingester.tests.unit.processor

import com.codingventures.movies.ingester.processor.ResponseProcessor
import com.codingventures.movies.ingester.remote.tmdb.response.MovieDetails as MovieDetailsResponse
import com.codingventures.movies.ingester.remote.tmdb.response.MovieList as MovieListResponse
import com.codingventures.movies.ingester.remote.tmdb.tasks.movieDetailsFetchTask
import com.codingventures.movies.ingester.remote.tmdb.tasks.personDetailsFetchTask
import com.codingventures.movies.ingester.remote.tmdb.tasks.popularMoviesFetchTask
import io.kotest.core.spec.style.ShouldSpec
import com.codingventures.movies.mockdata.response.movieAdAstra
import com.codingventures.movies.mockdata.response.movieListPage1
import com.codingventures.movies.mockdata.response.movieListPage2
import io.kotest.core.spec.Spec
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.shouldBe
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration

class ResponseProcessorTests : ShouldSpec() {
    private val responseProcessor =
        ResponseProcessor.initialize(
            maxNoPages = 10,
            maxNoCrewRequests = 2,
            maxNoCastRequests = 2
        )

    private lateinit var movieListFirstPage: MovieListResponse
    private lateinit var movieListLastPage: MovieListResponse
    private lateinit var movieDetailsAdAstra: MovieDetailsResponse

    override fun beforeSpec(spec: Spec) {
        super.beforeSpec(spec)
        val json = Json(JsonConfiguration.Stable.copy(isLenient = true, ignoreUnknownKeys = true))
        movieListLastPage = json.parse(MovieListResponse.serializer(),
            movieListPage2
        )
        movieListFirstPage = json.parse(MovieListResponse.serializer(),
            movieListPage1
        )

        movieDetailsAdAstra = json.parse(MovieDetailsResponse.serializer(),
            movieAdAstra
        )
    }

    init {
        should("prepare next page of movie list fetch task") {
            val expectedTasks = listOf(
                popularMoviesFetchTask(2),
                movieDetailsFetchTask("419704"),
                movieDetailsFetchTask("475557")
            )
            val result = responseProcessor.process(movieListFirstPage)
            result.additionalTasks shouldContainExactlyInAnyOrder  expectedTasks
        }
        should("return no additional movie list tasks if next page is more than total pages"){
            val expectedTasks = listOf(
                movieDetailsFetchTask("496243")
            )
            val result = responseProcessor.process(movieListLastPage)
            result.additionalTasks shouldBe expectedTasks
        }
        should("prepare person details fetch tasks") {
            val expectedTasks = listOf(
                personDetailsFetchTask("287"),
                personDetailsFetchTask("2176"),
                personDetailsFetchTask("20561")
            )
            val result = responseProcessor.process(movieDetailsAdAstra)
            result.additionalTasks shouldContainExactlyInAnyOrder expectedTasks
        }
    }
}