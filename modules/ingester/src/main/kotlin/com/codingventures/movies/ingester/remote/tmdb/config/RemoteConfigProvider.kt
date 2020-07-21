package com.codingventures.movies.ingester.remote.tmdb.config

import com.sksamuel.hoplite.ConfigLoader

data class RemoteConfigProvider(
    val apiKey: String,
    val host: String,
    val port: Int?,
    val https: Boolean = true) {
    companion object {
        fun default(): RemoteConfigProvider {
            return ConfigLoader().loadConfigOrThrow("/services.yaml")
        }
    }
}