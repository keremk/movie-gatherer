package com.codingventures.movies.ingester.remote.tmdb.fetchers

import com.codingventures.movies.ingester.reader.FetchTask
import com.codingventures.movies.ingester.reader.TaskType
import com.codingventures.movies.ingester.remote.tmdb.response.MovieList
import com.codingventures.movies.ingester.remote.tmdb.response.MovieDetails
import com.codingventures.movies.ingester.remote.tmdb.response.PersonDetails
import com.codingventures.movies.ingester.remote.tmdb.response.TmdbResponse
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.get
import io.ktor.http.URLBuilder
import io.ktor.http.URLProtocol
import io.ktor.util.valuesOf
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration

data class TmdbConfig(val apiKey: String, val host: String)

class TmdbClient(val httpClient: HttpClient, val configuration: TmdbConfig, val task: FetchTask) {
    suspend fun fetchData(): TmdbResponse = when (task.taskType) {
        TaskType.MovieList -> fetchMovieList()
        TaskType.MovieDetails -> fetchMovieDetails()
        TaskType.PersonDetails -> fetchPersonDetails()
    }

    private fun buildUrl(task: FetchTask): URLBuilder.(URLBuilder) -> Unit = {
            protocol = URLProtocol.HTTPS
            host = configuration.host
            encodedPath = task.endpoint.path
            parameters.appendAll(valuesOf(task.endpoint.params))
            parameters.append("api_key", configuration.apiKey)
        }

    suspend private fun fetchMovieList() = httpClient.get<MovieList> {
        url(buildUrl(task))
    }

    suspend private fun fetchMovieDetails() = httpClient.get<MovieDetails> {
        url(buildUrl(task))
    }

    suspend private fun fetchPersonDetails() = httpClient.get<PersonDetails>{
        url(buildUrl(task))
    }
}