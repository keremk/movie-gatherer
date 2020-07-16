package com.codingventures.movies.ingester

import com.codingventures.movies.ingester.pipeline.Pipeline
import io.ktor.util.KtorExperimentalAPI
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking

@KtorExperimentalAPI
@ExperimentalCoroutinesApi
fun main(args : Array<String>) {
    runBlocking {
        Pipeline.initialize().run()
    }
}
