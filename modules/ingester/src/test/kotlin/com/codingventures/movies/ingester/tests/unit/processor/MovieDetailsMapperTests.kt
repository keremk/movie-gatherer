package com.codingventures.movies.ingester.tests.unit.processor

import com.codingventures.movies.domain.*
import com.codingventures.movies.ingester.processor.*
import com.codingventures.movies.ingester.remote.tmdb.response.*
import com.codingventures.movies.ingester.remote.tmdb.response.Release as ReleaseResponse
import com.codingventures.movies.ingester.remote.tmdb.response.Genre as GenreResponse
import com.codingventures.movies.ingester.remote.tmdb.response.Crew as CrewResponse
import com.codingventures.movies.ingester.remote.tmdb.response.Cast as CastResponse
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import java.time.LocalDate

class MovieDetailsMapperTests : ShouldSpec() {
    private val releaseDates = ReleaseDates(
        results = listOf(
            ReleasedCountry(countryName = "FR", releases = listOf(
                ReleaseResponse(
                    certification = "12",
                    language = "fr",
                    note = "",
                    releaseDate = "2016-10-19T00:00:00.000Z",
                    type = 3
                )
            )),
            ReleasedCountry(countryName = "FR", releases = listOf(
                ReleaseResponse(
                    certification = "BOG", // bogus certification
                    language = "bo", //bogus code
                    note = "",
                    releaseDate = "2016-10-19T00:00:00.000Z",
                    type = 1
                )
            )),
            ReleasedCountry(countryName = "GB", releases = listOf(
                ReleaseResponse(
                    certification = "12",
                    language = "", // empty
                    note = "DVD",
                    releaseDate = "2017-02-27T00:00:00.000Z",
                    type = 5
                ),
                ReleaseResponse(
                    certification = "12A",
                    language = null, // null
                    note = null,
                    releaseDate = "2016-10-21T00:00:00.000Z",
                    type = 3
                )
            ))
    ))
    private val credits = Credits(
        cast = listOf(
            CastResponse(
                castId = 0,
                character = "Jack Reacher",
                creditId = "5573971c9251413f6600024d",
                gender = 2,
                id = 500,
                name = "Tom Cruise",
                order = 0,
                profilePath = "/3xuBtKtQwAUnEAoMfYseNSFhRkq.jpg"
            )
        ),
        crew = listOf(
            CrewResponse(
                creditId = "55fdcfa7c3a368133400196b",
                department = "Production",
                gender = 2,
                id = 500,
                job = "Producer",
                name = "Tom Cruise",
                profilePath = "/3xuBtKtQwAUnEAoMfYseNSFhRkq.jpg"
            )
        )
    )

    init {
        should("group and map release dates by country") {
            val expectedReleases = mapOf<String, List<Release>>(
                "FR" to listOf(
                    Release(
                        date = LocalDate.of(2016, 10, 19),
                        certification = Certification.Rated_12,
                        releaseType = ReleaseType.Theatrical,
                        language = LanguageCode.fr
                    ),
                    Release(
                        date = LocalDate.of(2016, 10, 19),
                        certification = Certification.Rated_Unspecified,
                        releaseType = ReleaseType.Premiere,
                        language = LanguageCode.bo
                    )),
                "GB" to listOf(
                    Release(
                        date = LocalDate.of(2017,2, 27),
                        certification = Certification.Rated_12,
                        releaseType = ReleaseType.Physical,
                        language = LanguageCode.unspecified
                    ),
                    Release(
                        date = LocalDate.of(2016,10, 21),
                        certification = Certification.Rated_12A,
                        releaseType = ReleaseType.Theatrical,
                        language = LanguageCode.unspecified
                    )
                )
            )
            val result = mapReleases(releaseDates)
            result shouldBe expectedReleases
        }
        should("map crew info from the movie response") {
            val expectedCrew = listOf(
                Crew(
                    urn = Urn("cv", "person", "500"),
                    jobRole = "Producer",
                    department = "Production"
                )
            )
            val result = mapCrew(credits)
            result shouldBe expectedCrew
        }
        should("map cast info from the movie response") {
            val expectedCast = listOf(
                Cast(
                    urn = Urn("cv", "person", "500"),
                    character = "Jack Reacher",
                    order = 0
                )
            )
            val result = mapCast(credits)
            result shouldBe expectedCast
        }
        should("map language codes from response") {
            val languagesResponse = listOf(
                Language(code = "de", name = "German"),
                Language(code = "bog", name = "Bogus"),
                Language(code = "", name = "Bogus")
            )
            val expectedLanguages = listOf(
                LanguageCode.de, LanguageCode.unspecified, LanguageCode.unspecified)
            val result = mapLanguages(languagesResponse)
            result shouldBe expectedLanguages
        }
        should("map company info from response") {
            val companyResponse = listOf(
                ProductionCompany(
                    id = 21777,
                    logoPath = null,
                    name = "TC Productions",
                    originCountry = "en"
                )
            )
            val expectedCompanies = listOf(
                Company(
                    urn = Urn("cv", "company", "21777")
                )
            )
            val result = mapCompanies(companyResponse)
            result shouldBe expectedCompanies
        }
        should("map countries from response") {
            val countryResponse = listOf(
                Country(code = "CN",name = "China"),
                Country(code = "", name = "Empty")
            )
            val expectedCountries = listOf(
                CountryCode.CN, CountryCode.Unspecified
            )
            val result = mapCountries(countryResponse)
            result shouldBe expectedCountries
        }
        should("map genres from response") {
            val genreResponse = listOf(
                GenreResponse(
                    id = 28,
                    name = "Action"
                ),
                GenreResponse(
                    id = 20,
                    name = "Drama"
                )
            )
            val expectedGenres = listOf(
                Genre(
                    id =  28,
                    name = "Action"
                ),
                Genre(
                    id = 20,
                    name = "Drama"
                )
            )
            val result = mapGenres(genreResponse)
            result shouldBe expectedGenres
        }
    }
}