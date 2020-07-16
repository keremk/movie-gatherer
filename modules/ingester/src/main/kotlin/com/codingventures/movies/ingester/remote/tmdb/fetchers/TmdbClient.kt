package com.codingventures.movies.ingester.remote.tmdb.fetchers

import com.codingventures.movies.domain.FetchTask
import com.codingventures.movies.domain.TaskType
import com.codingventures.movies.ingester.remote.tmdb.config.RemoteConfigProvider
import com.codingventures.movies.ingester.remote.tmdb.response.MovieList
import com.codingventures.movies.ingester.remote.tmdb.response.MovieDetails
import com.codingventures.movies.ingester.remote.tmdb.response.PersonDetails
import com.codingventures.movies.ingester.remote.tmdb.response.TmdbResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.http.URLBuilder
import io.ktor.http.URLProtocol
import io.ktor.util.valuesOf

interface RemoteClient {
    val httpClient: HttpClient
    val configuration: RemoteConfigProvider
    suspend fun fetchData(task: FetchTask): TmdbResponse
}

typealias BuildUrl = URLBuilder.(URLBuilder) -> Unit

data class FetchOperationException(
    val task: FetchTask,
    val inner: Exception
): Exception()

class TmdbClient(override val httpClient: HttpClient, override val configuration: RemoteConfigProvider): RemoteClient {
    suspend override fun fetchData(task: FetchTask): TmdbResponse  {
        try {
            return when (task.taskType) {
                TaskType.MovieList -> fetchMovieList(buildUrl(task))
                TaskType.MovieDetails -> fetchMovieDetails(buildUrl(task))
                TaskType.PersonDetails -> fetchPersonDetails(buildUrl(task))
            }
        } catch (e: Exception) {
            throw FetchOperationException(task, e)
        }
    }

    private fun buildUrl(task: FetchTask): BuildUrl = {
            protocol = if (configuration.https) URLProtocol.HTTPS else URLProtocol.HTTP
            host = configuration.host
            configuration.port?.let { port = configuration.port }
            encodedPath = task.endpoint.path
            parameters.appendAll(valuesOf(task.endpoint.params))
            parameters.append("api_key", configuration.apiKey)
        }

    suspend private fun fetchMovieList(buildUrl: BuildUrl) =
        httpClient.get<MovieList> {
            url(buildUrl)
        }

    suspend private fun fetchMovieDetails(buildUrl: BuildUrl) =
        httpClient.get<MovieDetails> {
            url(buildUrl)
        }


    suspend private fun fetchPersonDetails(buildUrl: BuildUrl) =
        httpClient.get<PersonDetails> {
            url(buildUrl)
        }
}