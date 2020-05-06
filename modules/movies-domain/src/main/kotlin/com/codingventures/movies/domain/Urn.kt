package com.codingventures.movies.domain

data class Urn(
    val org: String,
    val type: String,
    val id: String
) {
    override fun toString() = "$org:$type:$id"

    companion object
}

fun createMovieUrn(id: Int): Urn = Urn("cv", "movie","$id")

fun createPersonUrn(id: Int): Urn = Urn("cv", "person", "$id")

fun createCompanyUrn(id: Int): Urn = Urn("cv", "company", "$id")

fun createTMDBMovieUrn(id: Int): Urn = Urn("tmdb", "movie", "$id")

fun createIMDBMovieUrn(id: String?): Urn = Urn("imdb", "movie", id ?: "null")

fun createTMDBPersonUrn(id: Int): Urn = Urn("tmdb", "person", "$id")

fun createIMDBPersonUrn(id: String?): Urn = Urn("imdb", "person", id ?: "null")

final class MalformedUrnException(message: String): Exception(message)