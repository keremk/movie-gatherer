package com.codingventures.movies.moviesdb

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import com.codingventures.movies.dbcommon.pipeline.Pipeline
import com.codingventures.movies.domain.MovieDetails
import com.codingventures.movies.moviesdb.config.KafkaTopicsProvider
import com.codingventures.movies.moviesdb.mapper.prepareStatements
import kotlinx.coroutines.ObsoleteCoroutinesApi

@ObsoleteCoroutinesApi
@ExperimentalCoroutinesApi
fun main(args : Array<String>) = runBlocking {
    val kafkaTopics = KafkaTopicsProvider.default()
    Pipeline.initialize<MovieDetails>(
        consumerTopic = kafkaTopics.movies,
        deadLettersTopic = kafkaTopics.deadLetters,
        deserializer = MovieDetails.serializer(),
        prepareStatements = ::prepareStatements
    ).run()
}

