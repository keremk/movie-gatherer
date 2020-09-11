package com.codingventures.movies.ingester.config

import com.sksamuel.hoplite.ConfigLoader

data class KafkaTopicsProvider(
    val movies: String,
    val people: String,
    val tasks: String,
    val deadLetters: String
) {
    fun toList(): List<String> = listOf(movies, people, tasks, deadLetters)

    companion object {
        fun default(): KafkaTopicsProvider {
            return ConfigLoader().loadConfigOrThrow("/kafka-topics.yaml")
        }
    }
}
