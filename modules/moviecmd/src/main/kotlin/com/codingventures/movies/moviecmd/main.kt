package com.codingventures.movies.moviecmd

import com.codingventures.movies.domain.MovieDetails
import com.codingventures.movies.domain.PersonDetails
import com.codingventures.movies.ingester.reader.FetchTask
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.choice
import com.sksamuel.avro4k.Avro

class Schemas: CliktCommand() {
    val schema by option().choice("people", "movie", "fetchtask")

    override fun run() {
        val schemaChoice = schema ?: return

        when (schemaChoice) {
            "people" -> outputPeopleSchema()
            "movie" -> outputMovieSchema()
            "fetchtask" -> outputFetchTaskSchema()
            else -> echo("Incorrect choice $schemaChoice")
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