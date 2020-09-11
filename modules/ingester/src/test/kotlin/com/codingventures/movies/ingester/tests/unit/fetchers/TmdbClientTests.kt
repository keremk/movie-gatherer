package com.codingventures.movies.ingester.tests.unit.fetchers

import com.codingventures.movies.ingester.remote.tmdb.config.RemoteConfigProvider
import com.codingventures.movies.ingester.remote.tmdb.fetchers.FetchOperationException
import com.codingventures.movies.ingester.remote.tmdb.fetchers.TmdbClient
import com.codingventures.movies.ingester.remote.tmdb.response.MovieDetails
import com.codingventures.movies.ingester.remote.tmdb.response.MovieList
import com.codingventures.movies.ingester.remote.tmdb.response.MovieReference
import com.codingventures.movies.ingester.remote.tmdb.response.PersonDetails
import com.codingventures.movies.ingester.remote.tmdb.tasks.movieDetailsFetchTask
import com.codingventures.movies.ingester.remote.tmdb.tasks.personDetailsFetchTask
import com.codingventures.movies.ingester.remote.tmdb.tasks.popularMoviesFetchTask
import com.codingventures.movies.mockdata.domain.mockBadRequestTask
import com.codingventures.movies.mockdata.domain.mockMissingFieldsTask
import com.codingventures.movies.mockdata.domain.mockUnexpectedResponseTask
import com.codingventures.movies.mockdata.response.*
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.beOfType
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.engine.mock.respondBadRequest
import io.ktor.client.features.ClientRequestException
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import kotlinx.serialization.MissingFieldException
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import kotlinx.serialization.json.JsonDecodingException

class TmdbClientTests : ShouldSpec() {
    private val mockHeaders = headersOf(
        "Content-Type" to listOf(ContentType.Application.Json.toString())
    )

    private val config = RemoteConfigProvider(
        apiKey = "1234",
        host = "example.com/api",
        port = null,
        https = true
    )
    private val httpClient = HttpClient(MockEngine) {
        engine {
            addHandler { request ->
                val path = request.url.encodedPath
                if (path.contains("3/discover/movie")) {
                    respond(mockLongMovieList, headers = mockHeaders)
                } else if (path.contains("3/person")) {
                    respond(personBrad, headers = mockHeaders)
                } else if (path.contains("3/movie")) {
                    respond(mockLongMovieDetailsResponse, headers = mockHeaders)
                } else if (path.contains("3/invalid")) {
                    respondBadRequest()
                } else if (path.contains("3/unexpected")) {
                    respond(mockUnexpectedMovieDetails, headers = mockHeaders)
                } else if (path.contains("3/missingfields")) {
                    respond(mockMissingFields, headers = mockHeaders)
                } else {
                    respond("{ \"error\": \"Unknown\" }", headers = mockHeaders)
                }
            }
        }
        install(JsonFeature) {
            serializer = KotlinxSerializer(
                Json(JsonConfiguration.Stable.copy(isLenient = true, ignoreUnknownKeys = true))
            )
        }
    }

    init {
        should("return movie list") {
            val tmdbClient = TmdbClient(
                httpClient,
                config
            )
            val result = tmdbClient.fetchData(popularMoviesFetchTask(1))
            result should beOfType<MovieList>()
            val movieList = result as MovieList
            movieList.page shouldBe 1
            movieList.totalResults shouldBe 10000
            movieList.results.count() shouldBe 20
            movieList.results[0] should beOfType<MovieReference>()
            movieList.results[0].id shouldBe 419704
        }
        should("return movie details") {
            val tmdbClient = TmdbClient(
                httpClient,
                config
            )
            val result = tmdbClient.fetchData(movieDetailsFetchTask("12345"))
            result should beOfType<MovieDetails>()
            val movieDetails = result as MovieDetails
            movieDetails.title shouldBe "Spenser Confidential"
        }
        should("return person details") {
            val tmdbClient = TmdbClient(
                httpClient,
                config
            )
            val result = tmdbClient.fetchData(personDetailsFetchTask("12345"))
            result should beOfType<PersonDetails>()
            val personDetails = result as PersonDetails
            personDetails.name shouldBe "Brad Pitt"
        }
        should("raise exception if some required fields are missing") {
            val tmdbClient = TmdbClient(
                httpClient,
                config
            )
            val exception = shouldThrow<FetchOperationException> {
                tmdbClient.fetchData(mockMissingFieldsTask)
            }
            exception.inner should beOfType<MissingFieldException>()
            exception.inner.message shouldContain "Field 'original_language' is required, but it was missing"
        }
        should("raise an exception for a bad request") {
            val tmdbClient = TmdbClient(
                httpClient,
                config
            )
            val exception = shouldThrow<FetchOperationException> {
                tmdbClient.fetchData(mockBadRequestTask)
            }
            exception.inner should beOfType<ClientRequestException>()
            (exception.inner as ClientRequestException).response.status shouldBe HttpStatusCode.BadRequest
        }
        should("raise an exception for unexpected response format") {
            val tmdbClient = TmdbClient(
                httpClient,
                config
            )
            val exception = shouldThrow<FetchOperationException> {
                tmdbClient.fetchData(mockUnexpectedResponseTask)
            }
            exception.inner should beOfType<JsonDecodingException>()
            exception.inner.message shouldContain "Unexpected JSON token at offset 12"
        }
    }
}