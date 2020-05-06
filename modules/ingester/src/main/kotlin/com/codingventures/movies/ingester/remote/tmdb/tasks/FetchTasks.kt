package com.codingventures.movies.ingester.remote.tmdb.tasks

import com.codingventures.movies.ingester.reader.Endpoint
import com.codingventures.movies.ingester.reader.FetchTask
import com.codingventures.movies.ingester.reader.ServiceProvider
import com.codingventures.movies.ingester.reader.TaskType

fun popularMoviesFetchTask(page: Int) = FetchTask(
    jobType = TaskType.MovieList,
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
    jobType = TaskType.MovieDetails,
    serviceProvider = ServiceProvider.TMDB,
    endpoint = Endpoint(
        path = "3/movie/$id",
        params = mapOf("append_to_response" to listOf("credits,release_dates"))
    )
)

fun personDetailsFetchTask(id: String) = FetchTask(
    jobType = TaskType.PersonDetails,
    serviceProvider = ServiceProvider.TMDB,
    endpoint = Endpoint(
        path = "3/person/$id",
        params = mapOf("append_to_response" to listOf("images"))
    )
)

