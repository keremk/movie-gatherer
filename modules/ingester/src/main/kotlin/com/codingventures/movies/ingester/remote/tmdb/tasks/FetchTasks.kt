package com.codingventures.movies.ingester.remote.tmdb.tasks

import com.codingventures.movies.domain.Endpoint
import com.codingventures.movies.domain.FetchTask
import com.codingventures.movies.domain.ServiceProvider
import com.codingventures.movies.domain.TaskType

fun popularMoviesFetchTask(page: Int) = FetchTask(
    taskType = TaskType.MovieList,
    serviceProvider = ServiceProvider.TMDB,
    endpoint = Endpoint(
        path = "3/discover/movie",
        params = mapOf(
            "page" to listOf("$page"),
            "sort_by" to listOf("popularity.desc")
        )
    )
)

fun movieDetailsFetchTask(id: String) = FetchTask(
    taskType = TaskType.MovieDetails,
    serviceProvider = ServiceProvider.TMDB,
    endpoint = Endpoint(
        path = "3/movie/$id",
        params = mapOf("append_to_response" to listOf("credits,release_dates"))
    )
)

fun personDetailsFetchTask(id: String) = FetchTask(
    taskType = TaskType.PersonDetails,
    serviceProvider = ServiceProvider.TMDB,
    endpoint = Endpoint(
        path = "3/person/$id",
        params = mapOf("append_to_response" to listOf("images"))
    )
)

