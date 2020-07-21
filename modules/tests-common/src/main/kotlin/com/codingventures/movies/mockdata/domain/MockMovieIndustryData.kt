package com.codingventures.movies.mockdata.domain

import com.codingventures.movies.domain.*
import java.time.LocalDate

val mockMovieDetails = MovieDetails(
    movieUrn = Urn("cv", "movie", "12345"),
    externalUrns = listOf(
        Urn("imdb", "movie", "1234")
    ),
    title = "Spenser Confidential",
    tagline = "The law has limits. They don't.",
    overview = "Spenser, a former Boston patrolman who just got out from prison,",
    genres = listOf(
        Genre(id = 35, name = "Comedy"),
        Genre(id = 28, name = "Action")
    ),
    originalTitle = "Spenser Confidential",
    originalLanguage = LanguageCode.en,
    spokenLanguages = listOf(
        LanguageCode.en,
        LanguageCode.es
    ),
    backdropPath = "/ftODZXaXpWtV5XFD8gS9n9KwLDr.jpg",
    posterPath = "/fePczipv6ZzDO2uoww4vTAu2Sq3.jpg",
    homepage = "https://www.netflix.com/title/81005492",
    runtime = 110,
    budget = 0.0,
    revenue = 10000000.0,
    releaseDate = LocalDate.of(2020, 3, 6),
    releases = mapOf(
        "US" to listOf(
            Release(
                date = LocalDate.of(2020, 3, 6),
                certification = Certification.Rated_R,
                releaseType = ReleaseType.Theatrical,
                language = LanguageCode.fr
            ),
            Release(
                date = LocalDate.of(2020, 3, 1),
                certification = Certification.Rated_Unspecified,
                releaseType = ReleaseType.Premiere,
                language = LanguageCode.unspecified
            )),
        "GB" to listOf(
            Release(
                date = LocalDate.of(2017,2, 27),
                certification = Certification.Rated_12,
                releaseType = ReleaseType.Physical,
                language = LanguageCode.unspecified
            )
        )
    ),
    productionStatus = ProductionStatus.Released,
    productionCompanies = listOf(
        Company(urn = Urn("cv", "company", "20153"))
    ),
    productionCountries = listOf(
        CountryCode.US
    ),
    cast = listOf(
        Cast(
            urn = Urn("cv", "person", "13240"),
            character = "Spenser",
            order = 0
        ),
        Cast(
            urn = Urn("cv", "person", "1447932"),
            character = "Hawk",
            order = 1
        ),
        Cast(
            urn = Urn("cv", "person", "1903"),
            character = "Henry",
            order = 2
        )
    ),
    crew = listOf(
        Crew(
            urn = Urn("cv", "person", "4723"),
            jobRole = "Screenplay",
            department = "Writing"
        ),
        Crew(
            urn = Urn("cv", "person", "500"),
            jobRole = "Production Design",
            department = "Art"
        ),
        Crew(
            urn = Urn("cv", "person", "7494"),
            jobRole = "Casting",
            department = "Production"
        )
    ),
    popularity = 36.585,
    voteAverage = 6.7,
    voteCount = 931
)

val mockPersonDetails = PersonDetails(
    personUrn = Urn("cv", "person", "117"),
    externalUrns = listOf(
        Urn("imdb", "person", "nm0000032")
    ),
    birthday = LocalDate.of(1923, 10, 4),
    deathday = LocalDate.of(2008, 4, 5),
    knownForDepartment = "Acting",
    name = "Charlton Heston",
    alsoKnownAs = emptyList(),
    gender = Gender.Male,
    biography = "Charlton Heston was an American actor of film, theatre and television.",
    popularity = 4.99,
    placeOfBirth = "Wilmette, Illinois, USA",
    profilePicturePath = "/yqmVz8wDDpb49SYdNNrmuEm9YL0.jpg",
    homepage = null,
    pictures = listOf(
        Picture(
            path = "/h19IAwRMISiOKO8DblAK4gEshom.jpg",
            width = 1106,
            height = 1660
        )
    )
)