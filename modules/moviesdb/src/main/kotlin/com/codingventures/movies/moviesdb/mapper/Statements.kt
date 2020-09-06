package com.codingventures.movies.moviesdb.mapper

import com.codingventures.movies.dbcommon.pipeline.StatementDeclaration
import com.codingventures.movies.domain.MovieDetails
import com.codingventures.movies.moviesdb.data.*

fun prepareStatements(movies: List<MovieDetails>): List<StatementDeclaration> {
    val statements = Statements(movies)

    return listOf(
        statements.insertMovies,
        statements.insertReleases,
        statements.insertCast,
        statements.insertCrew
    )
}

class Statements(val movies: List<MovieDetails>) {
    val insertMovies by lazy {
        StatementDeclaration(
            insertStatement = MovieDbHelper.generateInsertStatement(),
            batchData = movies
                .map { Mappers.mapMovie(it) }
                .map { it.valuesAsList() as List<Any> }
        )
    }

    val insertReleases by lazy {
        StatementDeclaration(
            insertStatement = ReleaseDbHelper.generateInsertStatement(),
            batchData = movies
                .map { Pair(it.movieUrn, it.releases) }
                .map { Mappers.mapReleases(it.first, it.second) }
                .flatten()
                .map { it.valuesAsList() }
        )
    }

    val insertCast by lazy {
        StatementDeclaration(
            insertStatement = CastDbHelper.generateInsertStatement(),
            batchData = movies
                .map { Pair(it.movieUrn, it.cast) }
                .map { Mappers.mapCast(it.first, it.second) }
                .flatten()
                .map { it.valuesAsList() }
        )
    }

    val insertCrew by lazy {
        StatementDeclaration(
            insertStatement = CrewDbHelper.generateInsertStatement(),
            batchData = movies
                .map { Pair(it.movieUrn, it.crew) }
                .map { Mappers.mapCrew(it.first, it.second) }
                .flatten()
                .map { it.valuesAsList() }
        )
    }
}