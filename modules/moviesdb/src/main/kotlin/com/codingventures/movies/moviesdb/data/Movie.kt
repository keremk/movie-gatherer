package com.codingventures.movies.moviesdb.data

import com.codingventures.annotations.Column
import com.codingventures.annotations.Table
import java.time.LocalDate

@Table("movies", primaryKey = "urn")
data class Movie(
    @Column("urn")
    val urn: String,
    @Column("external_urns")
    val externalUrns: Array<String>,
    @Column("title")
    val title: String,
    @Column("overview")
    val overview: String,
    @Column("genres")
    val genres: Array<Int>,
    @Column("original_title")
    val originalTitle: String,
    @Column("original_language")
    val originalLanguage: String,
    @Column("spoken_languages")
    val spokenLanguages: Array<String>,
    @Column("backdrop_path")
    val backdropPath: String?,
    @Column("poster_path")
    val posterPath: String?,
    @Column("homepage")
    val homePage: String?,
    @Column("runtime")
    val runtime: Int,
    @Column("budget")
    val budget: Double,
    @Column("revenue")
    val revenue: Double,
    @Column("release_date")
    val releaseDate: LocalDate,
    @Column("production_status")
    val productionStatus: String,
    @Column("production_countries")
    val productionCountries: Array<String>,
    @Column("production_company_urns")
    val productionCompanyUrns: Array<String>,
    @Column("popularity")
    val popularity: Double,
    @Column("vote_average")
    val voteAverage: Double,
    @Column("vote_count")
    val voteCount: Int
)

