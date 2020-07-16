package com.codingventures.movies.domain

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FetchTask(
    @SerialName("task_type")
    val taskType: TaskType,
    @SerialName("service_provider")
    val serviceProvider: ServiceProvider,
    @SerialName("endpoint")
    val endpoint: Endpoint
)

@Serializable
enum class TaskType {
    MovieList,
    MovieDetails,
    PersonDetails
}

@Serializable
enum class ServiceProvider {
    TMDB
}

@Serializable
data class Endpoint(
    val path: String,
    val params: Map<String, List<String>> = emptyMap()
)