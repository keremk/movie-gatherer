package com.codingventures.movies.moviesdb.mapper

import com.codingventures.movies.domain.*
import com.codingventures.movies.domain.Release as ReleaseDomain
import com.codingventures.movies.domain.Cast as CastDomain
import com.codingventures.movies.domain.Crew as CrewDomain
import com.codingventures.movies.moviesdb.data.Cast
import com.codingventures.movies.moviesdb.data.Crew
import com.codingventures.movies.moviesdb.data.Movie
import com.codingventures.movies.moviesdb.data.Release

object Mappers {
    fun mapMovie(movieDetails: MovieDetails) =
        Movie(
            urn = movieDetails.movieUrn.toString(),
            externalUrns = mapExternalUrns(movieDetails.externalUrns),
            title = movieDetails.title,
            overview = movieDetails.overview,
            genres = mapGenres(movieDetails.genres),
            originalTitle = movieDetails.originalTitle,
            originalLanguage = movieDetails.originalLanguage.code,
            spokenLanguages = mapSpokenLanguages(movieDetails.spokenLanguages),
            backdropPath = movieDetails.backdropPath,
            posterPath = movieDetails.posterPath,
            homePage = movieDetails.homepage,
            runtime = movieDetails.runtime,
            budget = movieDetails.budget,
            revenue = movieDetails.revenue,
            releaseDate = movieDetails.releaseDate,
            productionStatus = movieDetails.productionStatus.status,
            productionCountries = mapProductionCountries(movieDetails.productionCountries),
            productionCompanyUrns = mapProductionCompanies(movieDetails.productionCompanies),
            popularity = movieDetails.popularity,
            voteAverage = movieDetails.voteAverage,
            voteCount = movieDetails.voteCount
        )

    fun mapReleases(movieUrn: Urn, releases: Map<String, List<ReleaseDomain>>): List<Release> =
        releases.entries.fold(emptyList()) { acc, entry ->
            val mappedReleases = entry.value.map {
                Release(
                    id = -1,
                    movieUrn = movieUrn.toString(),
                    countryCode = entry.key,
                    releaseDate = it.date,
                    spokenLanguage = it.language.code,
                    certification = it.certification.value,
                    releaseType = it.releaseType.type
                )
            }
            acc.plus(mappedReleases)
        }

    fun mapCast(movieUrn: Urn, cast: List<CastDomain>): List<Cast> {
        return cast.map{
            Cast(
                id = -1,
                movieUrn = movieUrn.toString(),
                personUrn = it.urn.toString(),
                characterName = it.character,
                ordinal = it.order
            )
        }
    }

    fun mapCrew(movieUrn: Urn, crew: List<CrewDomain>): List<Crew> {
        return crew.map {
            Crew(
                id = -1,
                movieUrn = movieUrn.toString(),
                personUrn = it.urn.toString(),
                jobRole = it.jobRole,
                department = it.department
            )
        }
    }

    private fun mapExternalUrns(urns: List<Urn>) = urns.map { it.toString() }.toTypedArray()
    private fun mapGenres(genres: List<Genre>) = genres.map { it.id }.toTypedArray()
    private fun mapSpokenLanguages(languages: List<LanguageCode>) = languages.map { it.code }.toTypedArray()
    private fun mapProductionCountries(countries: List<CountryCode>) = countries.map { it.code }.toTypedArray()
    private fun mapProductionCompanies(companies: List<Company>) = companies.map { it.urn.toString() }.toTypedArray()
}