package com.codingventures.movies.ingester.remote

import com.codingventures.movies.ingester.reader.FetchTask
import com.codingventures.movies.ingester.remote.tmdb.fetchers.TmdbClient
import com.codingventures.movies.ingester.remote.tmdb.fetchers.TmdbConfig
import com.codingventures.movies.ingester.remote.tmdb.response.TmdbResponse
import com.sksamuel.hoplite.ConfigLoader
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.util.KtorExperimentalAPI
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import mu.KotlinLogging

private val logger = KotlinLogging.logger {}

@KtorExperimentalAPI
suspend fun fetchData(task: FetchTask): TmdbResponse {
    val httpClient = HttpClient(CIO) {
        install(JsonFeature) {
            serializer = KotlinxSerializer(
                Json(JsonConfiguration.Stable.copy(isLenient = true, ignoreUnknownKeys = true)))
        }
    }
    val config = ConfigLoader().loadConfigOrThrow<TmdbConfig>("/services.yaml")

    return TmdbClient(httpClient, config, task).fetchData()
}

