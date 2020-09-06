package com.codingventures.movies.mockdata.domain

import com.codingventures.movies.domain.*
import java.time.LocalDate

val mockMovies = listOf(
    MovieDetails(
        movieUrn = Urn("cv", "movie", "12345"),
        externalUrns = listOf(
            Urn("imdb", "movie", "1234")
        ),
        title = "Spenser Confidential",
        tagline = "The law has limits. They don't.",
        overview = "Spenser a former Boston patrolman who just got out from prison",
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
                )
            ),
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
    ),
    MovieDetails(
        movieUrn = Urn("cv", "movie", "475557"),
        externalUrns = listOf(
            Urn("imdb", "movie", "tt7286456")
        ),
        title = "Joker",
        tagline = "Put on a happy face",
        overview = "During the 1980s a failed stand-up comedian is driven insane ...",
        genres = listOf(
            Genre(id = 80, name = "Crime"),
            Genre(id = 53, name = "Thriller"),
            Genre(id = 18, name = "Drama")
            ),
        originalTitle = "Joker",
        originalLanguage = LanguageCode.en,
        spokenLanguages = listOf(
            LanguageCode.en
        ),
        backdropPath = null,
        posterPath = "/udDclJoHjfjb8Ekgsd4FDteOkCU.jpg",
        homepage = null,
        runtime = 122,
        budget = 55000000.0,
        revenue = 1074251311.0,
        releaseDate = LocalDate.of(2020, 3, 6),
        releases = mapOf(
            "US" to listOf(
                Release(
                    date = LocalDate.of(2019, 10, 4),
                    certification = Certification.Rated_R,
                    releaseType = ReleaseType.Theatrical,
                    language = LanguageCode.en
                ),
                Release(
                    date = LocalDate.of(2020, 1, 7),
                    certification = Certification.Rated_Unspecified,
                    releaseType = ReleaseType.Digital,
                    language = LanguageCode.unspecified
                )),
            "GB" to listOf(
                Release(
                    date = LocalDate.of(2019,10, 4),
                    certification = Certification.Rated_15,
                    releaseType = ReleaseType.Theatrical,
                    language = LanguageCode.unspecified
                )
            )
        ),
        productionStatus = ProductionStatus.Released,
        productionCompanies = listOf(
            Company(urn = Urn("cv", "company", "9993")),
            Company(urn = Urn("cv", "company", "174"))
        ),
        productionCountries = listOf(
            CountryCode.US, CountryCode.CN
        ),
        cast = listOf(
            Cast(
                urn = Urn("cv", "person", "73421"),
                character = "Arthur Fleck / Joker",
                order = 0
            ),
            Cast(
                urn = Urn("cv", "person", "380"),
                character = "Murray Franklin",
                order = 1
            ),
            Cast(
                urn = Urn("cv", "person", "1545693"),
                character = "Zazie Beetz",
                order = 2
            ),
            Cast(
                urn = Urn("cv", "person", "4432"),
                character = "Penny Fleck",
                order = 2
            )
        ),
        crew = listOf(
            Crew(
                urn = Urn("cv", "person", "324"),
                jobRole = "Writer",
                department = "Writing"
            ),
            Crew(
                urn = Urn("cv", "person", "1296"),
                jobRole = "Executive Producer",
                department = "Production"
            ),
            Crew(
                urn = Urn("cv", "person", "3794"),
                jobRole = "Characters",
                department = "Writing"
            )
        ),
        popularity = 126.041,
        voteAverage = 8.2,
        voteCount = 14466
    ),
    MovieDetails(
        movieUrn = Urn("cv", "movie", "577922"),
        externalUrns = listOf(
            Urn("imdb", "movie", "tt6723592")
        ),
        title = "Tenet",
        tagline = "Time runs out.",
        overview = "Armed with only one word - Tenet - and fighting for the survival of the entire ",
        genres = listOf(
            Genre(id = 53, name = "Thriller"),
            Genre(id = 28, name = "Action")
        ),
        originalTitle = "Tenet",
        originalLanguage = LanguageCode.en,
        spokenLanguages = listOf(
            LanguageCode.en
        ),
        backdropPath = "/wzJRB4MKi3yK138bJyuL9nx47y6.jpg",
        posterPath = null,
        homepage = "https://www.tenetfilm.com/",
        runtime = 150,
        budget = 205000000.0,
        revenue = 0.0,
        releaseDate = LocalDate.of(2020, 8, 22),
        releases = mapOf(
            "FR" to listOf(
                Release(
                    date = LocalDate.of(2020, 8, 26),
                    certification = Certification.Rated_U,
                    releaseType = ReleaseType.Theatrical,
                    language = LanguageCode.fr
                )
            ),
            "US" to listOf(
                Release(
                    date = LocalDate.of(2020,8, 26),
                    certification = Certification.Rated_PG13,
                    releaseType = ReleaseType.Theatrical,
                    language = LanguageCode.unspecified
                )
            )
        ),
        productionStatus = ProductionStatus.Released,
        productionCompanies = listOf(
            Company(urn = Urn("cv", "company", "9996")),
            Company(urn = Urn("cv", "company", "174"))
        ),
        productionCountries = listOf(
            CountryCode.US,
            CountryCode.GB,
            CountryCode.CA,
            CountryCode.EE,
            CountryCode.NO
        ),
        cast = listOf(
            Cast(
                urn = Urn("cv", "person", "1117313"),
                character = "The Protagonist",
                order = 0
            ),
            Cast(
                urn = Urn("cv", "person", "11288"),
                character = "Neil",
                order = 1
            )
        ),
        crew = listOf(
            Crew(
                urn = Urn("cv", "person", "525"),
                jobRole = "Directing",
                department = "Director"
            )
        ),
        popularity = 275.087,
        voteAverage = 7.7,
        voteCount = 214
    )
)