package com.codingventures.movies.moviesdb

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import com.codingventures.movies.dbcommon.pipeline.Pipeline
import com.codingventures.movies.domain.MovieDetails
import com.codingventures.movies.moviesdb.mapper.prepareStatements
import kotlinx.coroutines.ObsoleteCoroutinesApi

@ObsoleteCoroutinesApi
@ExperimentalCoroutinesApi
fun main(args : Array<String>) = runBlocking {
    Pipeline.initialize<MovieDetails>(
        consumerTopic = "movies",
        deserializer = MovieDetails.serializer(),
        prepareStatements = ::prepareStatements
    ).run()
}

