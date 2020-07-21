package com.codingventures.movies.mockdata.domain

import com.codingventures.movies.domain.Endpoint
import com.codingventures.movies.domain.FetchTask
import com.codingventures.movies.domain.ServiceProvider
import com.codingventures.movies.domain.TaskType

val mockBadRequestTask = FetchTask(
    taskType = TaskType.MovieDetails,
    serviceProvider = ServiceProvider.TMDB,
    endpoint = Endpoint(
        path = "3/invalid",
        params = mapOf("append_to_response" to listOf("credits,release_dates"))
    )
)

val mockUnexpectedResponseTask = FetchTask(
    taskType = TaskType.MovieDetails,
    serviceProvider = ServiceProvider.TMDB,
    endpoint = Endpoint(
        path = "3/unexpected",
        params = mapOf("append_to_response" to listOf("credits,release_dates"))
    )
)

val mockMissingFieldsTask = FetchTask(
    taskType = TaskType.MovieDetails,
    serviceProvider = ServiceProvider.TMDB,
    endpoint = Endpoint(
        path = "3/missingfields",
        params = mapOf("append_to_response" to listOf("credits,release_dates"))
    )
)