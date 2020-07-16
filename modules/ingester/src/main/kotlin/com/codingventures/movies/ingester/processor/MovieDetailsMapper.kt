package com.codingventures.movies.ingester.processor

import com.codingventures.movies.domain.*
import com.codingventures.movies.domain.Cast
import com.codingventures.movies.domain.Crew
import com.codingventures.movies.domain.Genre
import com.codingventures.movies.domain.Release
import com.codingventures.movies.ingester.remote.tmdb.response.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import com.codingventures.movies.ingester.remote.tmdb.response.MovieDetails as MovieDetailsResponse
import com.codingventures.movies.ingester.remote.tmdb.response.Genre as GenreResponse

fun mapMovieResponseToMovieDetails(response: MovieDetailsResponse): MovieDetails {
    return MovieDetails(
        movieUrn = createMovieUrn(response.tmdbId),
        externalUrns = listOf(
            createTMDBMovieUrn(response.tmdbId),
            createIMDBMovieUrn(response.imdbId)
        ),

        title = response.title,
        tagline = response.tagline ?: "",
        overview = response.overview ?: "",
        genres = mapGenres(response.genres),
        originalTitle = response.originalTitle,
        originalLanguage = LanguageCode.fromString(response.originalLanguage),
        spokenLanguages = mapLanguages(response.spokenLanguages),
        backdropPath = response.backdropPath,
        posterPath = response.posterPath,
        homepage = response.homepage,
        runtime = response.runtime ?: 0,

        budget = response.budget,
        revenue = response.revenue.toDouble(),

        releaseDate = LocalDate.parse(response.releaseDate, DateTimeFormatter.ISO_LOCAL_DATE),
        releases = mapReleases(response.releaseDates),
        productionStatus = ProductionStatus.fromString(response.status),
        productionCompanies = mapCompanies(response.productionCompanies),
        productionCountries = mapCountries(response.productionCountries),

        crew = mapCrew(response.credits),
        cast = mapCast(response.credits),

        popularity = response.popularity.toDouble(),
        voteAverage = response.voteAverage,
        voteCount = response.voteCount
    )
}

fun mapReleases(response: ReleaseDates): Map<String, List<Release>> {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    val releaseMap = response.results.groupBy(
        { it.countryName.toString() },
        { it.releases.map{ release ->
            Release(
                date = LocalDate.parse(release.releaseDate, formatter), // 2016-10-21T00:00:00.000Z
                certification = Certification.fromString(release.certification),
                releaseType = ReleaseType.fromInt(release.type),
                language = LanguageCode.fromString(release.language ?: "")
            )
        }}
    )
    return releaseMap.mapValues { v -> v.value.flatten() }
}

fun mapCrew(credits: Credits): List<Crew> {
    return credits.crew.map { crew -> Crew(
        urn = createPersonUrn(crew.id),
        jobRole = crew.job,
        department = crew.department
    )}
}

fun mapCast(credits: Credits): List<Cast> {
    return credits.cast.map { cast -> Cast(
        urn = createPersonUrn(cast.id),
        character = cast.character,
        order = cast.order
    )}
}

fun mapLanguages(languages: List<Language>): List<LanguageCode> {
    return languages.map { language -> LanguageCode.fromString(language.code)}
}

fun mapCompanies(companies: List<ProductionCompany>): List<Company> {
    return companies.map { company -> Company(
        urn = createCompanyUrn(company.id)
    )}
}

fun mapCountries(countries: List<Country>): List<CountryCode> {
    return countries.map { country -> CountryCode.fromString(country.code) }
}

fun mapGenres(genres: List<GenreResponse>): List<Genre> {
    return genres.map { genre -> Genre(
        id = genre.id,
        name = genre.name
    )}
}