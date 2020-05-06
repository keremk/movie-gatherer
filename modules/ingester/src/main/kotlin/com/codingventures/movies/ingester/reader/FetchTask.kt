package com.codingventures.movies.ingester.reader

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FetchTask(
    @SerialName("job_type")
    val jobType: TaskType,
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

