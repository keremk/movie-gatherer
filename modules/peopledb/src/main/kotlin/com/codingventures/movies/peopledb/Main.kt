package com.codingventures.movies.peopledb

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import com.codingventures.movies.dbcommon.pipeline.Pipeline
import com.codingventures.movies.domain.PersonDetails
import com.codingventures.movies.peopledb.config.KafkaTopicsProvider
import com.codingventures.movies.peopledb.mapper.prepareStatements
import kotlinx.coroutines.ObsoleteCoroutinesApi

@ObsoleteCoroutinesApi
@ExperimentalCoroutinesApi
fun main(args : Array<String>) = runBlocking {
    val kafkaTopicsProvider = KafkaTopicsProvider.default()
    Pipeline.initialize<PersonDetails>(
        consumerTopic = kafkaTopicsProvider.people,
        deadLettersTopic = kafkaTopicsProvider.deadLetters,
        deserializer = PersonDetails.serializer(),
        prepareStatements = ::prepareStatements
    ).run()
}

