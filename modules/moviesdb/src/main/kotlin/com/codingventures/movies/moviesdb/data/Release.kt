package com.codingventures.movies.moviesdb.data

import com.codingventures.annotations.Column
import com.codingventures.annotations.Table
import java.time.LocalDate

@Table("releases", primaryKey = "id")
data class Release(
    @Column("id", exclude = true)
    val id: Int,
    @Column("movie_urn")
    val movieUrn: String,
    @Column("country_code")
    val countryCode: String,
    @Column("release_date")
    val releaseDate: LocalDate,
    @Column("spoken_language")
    val spokenLanguage: String,
    @Column("certification")
    val certification: String,
    @Column("release_type")
    val releaseType: Int
)