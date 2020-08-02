package com.codingventures.movies.ingester.remote.tmdb.config

import com.sksamuel.hoplite.ConfigLoader

data class RemoteConfigProvider(
    val apiKey: String,
    val host: String,
    val port: Int?,
    val https: Boolean = true,
    val maxNoPages: Int = Int.MAX_VALUE,
    val maxNoCrewRequests: Int = Int.MAX_VALUE,
    val maxNoCastRequests: Int = Int.MAX_VALUE
) {
    companion object {
        fun default(): RemoteConfigProvider {
            return ConfigLoader().loadConfigOrThrow("/services.yaml")
        }
    }
}