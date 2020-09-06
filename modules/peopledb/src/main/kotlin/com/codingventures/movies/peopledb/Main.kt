package com.codingventures.movies.peopledb

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import com.codingventures.movies.dbcommon.pipeline.Pipeline
import com.codingventures.movies.domain.PersonDetails
import com.codingventures.movies.peopledb.mapper.prepareStatements
import kotlinx.coroutines.ObsoleteCoroutinesApi

@ObsoleteCoroutinesApi
@ExperimentalCoroutinesApi
fun main(args : Array<String>) = runBlocking {
    Pipeline.initialize<PersonDetails>(
        consumerTopic = "people",
        deserializer = PersonDetails.serializer(),
        prepareStatements = ::prepareStatements
    ).run()
}

