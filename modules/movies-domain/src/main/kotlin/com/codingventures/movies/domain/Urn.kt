package com.codingventures.movies.domain

data class Urn(
    val org: String,
    val type: String,
    val id: String
) {
    override fun toString() = "$org:$type:$id"

    companion object {
        fun fromString(urnString: String): Urn {
            val parts = urnString.split(":")
            if (parts.count() < 3) {
                throw MalformedUrnException("Text '$urnString' is an unknown urn format")
            }

            if (parts[0].isEmpty() || parts[1].isEmpty() || parts[2].isEmpty()) {
                throw MalformedUrnException("Text '$urnString' is missing some fields")
            }

            return Urn(
                org = parts[0],
                type = parts[1],
                id = parts[2]
            )
        }

        fun create(org: String, type: String, id: String?): Urn? {
            if (id == null) return null
            if (id.isEmpty()) return null

            return Urn(org, type, id)
        }
    }
}

fun createMovieUrn(id: Int): Urn = Urn("cv", "movie","$id")

fun createPersonUrn(id: Int): Urn = Urn("cv", "person", "$id")

fun createCompanyUrn(id: Int): Urn = Urn("cv", "company", "$id")

fun createTMDBMovieUrn(id: Int): Urn = Urn("tmdb", "movie", "$id")

fun createIMDBMovieUrn(id: String?): Urn? = Urn.create("imdb", "movie", id)

fun createTMDBPersonUrn(id: Int): Urn = Urn("tmdb", "person", "$id")

fun createIMDBPersonUrn(id: String?): Urn? = Urn.create("imdb", "person", id)

class MalformedUrnException(message: String): Exception(message)