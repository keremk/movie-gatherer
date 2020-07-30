package com.codingventures.movies.moviecmd

import com.codingventures.movies.domain.MovieDetails
import com.codingventures.movies.domain.PersonDetails
import com.codingventures.movies.domain.FetchTask
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.choice
import com.sksamuel.avro4k.Avro
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import org.apache.avro.Schema
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.post
import io.ktor.http.URLBuilder
import io.ktor.http.URLProtocol
import io.ktor.util.KtorExperimentalAPI
import kotlinx.coroutines.coroutineScope

typealias BuildUrl = URLBuilder.(URLBuilder) -> Unit

class Schemas : CliktCommand() {
    private val schema by option().choice("people", "movie", "fetchtask")

    @KtorExperimentalAPI
    override fun run() {
        val schemaChoice = schema ?: return

        when (schemaChoice) {
            "people" -> outputPeopleSchema()
            "movie" -> outputMovieSchema()
            "fetchtask" -> outputFetchTaskSchema()
            "register" -> registerSchemas()
            else -> echo("Incorrect choice $schemaChoice")
        }
    }

    @KtorExperimentalAPI
    fun registerSchemas() = runBlocking {
        val schemaList = mapOf(
            "Tasks" to Avro.default.schema(FetchTask.serializer()),
            "People" to Avro.default.schema(PersonDetails.serializer()),
            "Movies" to Avro.default.schema(MovieDetails.serializer())
        )

        schemaList.forEach {
            registerSchema(it.key, it.value)
        }
    }

    private fun buildUrl(schemaName: String): BuildUrl = {
        protocol = URLProtocol.HTTP
        host = "localhost"
        port = 8081
        encodedPath = "/subjects/$schemaName-value/versions"
    }

    @KtorExperimentalAPI
    private suspend fun registerSchema(name: String, schemaDesc: Schema) = coroutineScope {
        val httpClient = HttpClient(CIO) {
            install(JsonFeature) {
                serializer = KotlinxSerializer(
                    Json(JsonConfiguration.Stable.copy(isLenient = true, ignoreUnknownKeys = true))
                )
            }
        }
        httpClient.post<String> {
            url(buildUrl(name))
            body = schemaDesc.toString(true)
        }
    }

    private fun outputPeopleSchema() {
        val schema = Avro.default.schema(PersonDetails.serializer())
        echo(schema.toString(true))
    }

    private fun outputMovieSchema() {
        val schema = Avro.default.schema(MovieDetails.serializer())
        echo(schema.toString(true))
    }

    private fun outputFetchTaskSchema() {
        val schema = Avro.default.schema(FetchTask.serializer())
        echo(schema.toString(true))
    }
}

fun main(args: Array<String>) = Schemas().main(args)