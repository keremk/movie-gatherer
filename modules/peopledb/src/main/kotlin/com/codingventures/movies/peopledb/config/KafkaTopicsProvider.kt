package com.codingventures.movies.peopledb.config

import com.sksamuel.hoplite.ConfigLoader

data class KafkaTopicsProvider(
    val people: String,
    val deadLetters: String
) {
    fun toList(): List<String> = listOf(people, deadLetters)

    companion object {
        fun default(): KafkaTopicsProvider {
            return ConfigLoader().loadConfigOrThrow("/kafka-topics.yaml")
        }
    }
}
