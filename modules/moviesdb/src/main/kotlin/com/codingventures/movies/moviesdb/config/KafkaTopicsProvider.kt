package com.codingventures.movies.moviesdb.config

import com.sksamuel.hoplite.ConfigLoader

data class KafkaTopicsProvider(
    val movies: String,
    val deadLetters: String
) {
    fun toList(): List<String> = listOf(movies, deadLetters)

    companion object {
        fun default(): KafkaTopicsProvider {
            return ConfigLoader().loadConfigOrThrow("/kafka-topics.yaml")
        }
    }
}
