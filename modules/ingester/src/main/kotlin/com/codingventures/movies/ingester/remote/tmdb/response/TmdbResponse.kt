package com.codingventures.movies.ingester.remote.tmdb.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

sealed class TmdbResponse
/*
{
    "page": 2,
    "total_results": 10000,
    "total_pages": 500,
    "results": [
        {
            "popularity": 146.803,
            "vote_count": 589,
            "video": false,
            "poster_path": "/jtrhTYB7xSrJxR1vusu99nvnZ1g.jpg",
            "id": 522627,
            "adult": false,
            "backdrop_path": "/9Qfawg9WT3cSbBXQgDRuWbYS9lj.jpg",
            "original_language": "en",
            "original_title": "The Gentlemen",
            "genre_ids": [
                28,
                35,
                80
            ],
            "title": "The Gentlemen",
            "vote_average": 7.8,
            "overview": "American expat Mickey Pearson has built a highly profitable marijuana empire in London. When word gets out that he’s looking to cash out of the business forever it triggers plots, schemes, bribery and blackmail in an attempt to steal his domain out from under him.",
            "release_date": "2019-12-16"
        },
        ...
    ]
}
 */
@Serializable
data class MovieList(
    @SerialName("page")
    val page: Int,
    @SerialName("total_results")
    val totalResults: Int,
    @SerialName("total_pages")
    val totalPages: Int,
    @SerialName("results")
    val results: List<MovieReference>
): TmdbResponse()

/*
{
    "adult": false,
    "backdrop_path": "/nDS8rddEK74HfAwCC5CoT6Cwzlt.jpg",
    "belongs_to_collection": {
        "id": 403374,
        "name": "Jack Reacher Collection",
        "poster_path": "/7baSUtFKi8PQ9SLo6ECYBfAW2K8.jpg",
        "backdrop_path": "/vViRXFnSyGJ2fzMbcc5sqTKswcd.jpg"
    },
    "budget": 60000000,
    "genres": [
        {
            "id": 28,
            "name": "Action"
        }, ...
    ],
    "homepage": "http://www.jackreachermovie.com/",
    "id": 343611,
    "imdb_id": "tt3393786",
    "original_language": "en",
    "original_title": "Jack Reacher: Never Go Back",
    "overview": "Jack Reacher must uncover the truth behind a major government conspiracy in order to clear his name. On the run as a fugitive from the law, Reacher uncovers a potential secret from his past that could change his life forever.",
    "popularity": 18.101,
    "poster_path": "/IfB9hy4JH1eH6HEfIgIGORXi5h.jpg",
    "production_companies": [
        {
            "id": 21777,
            "logo_path": null,
            "name": "TC Productions",
            "origin_country": ""
        },...
    ],
    "production_countries": [
        {
            "iso_3166_1": "CN",
            "name": "China"
        }, ...
    ],
    "release_date": "2016-10-19",
    "revenue": 162146076,
    "runtime": 118,
    "spoken_languages": [
        {
            "iso_639_1": "en",
            "name": "English"
        }
    ],
    "status": "Released",
    "tagline": "Justice is Coming.",
    "title": "Jack Reacher: Never Go Back",
    "video": false,
    "vote_average": 5.6,
    "vote_count": 2974,
    "credits": {
        "cast": [
            {
                "cast_id": 0,
                "character": "Jack Reacher",
                "credit_id": "5573971c9251413f6600024d",
                "gender": 2,
                "id": 500,
                "name": "Tom Cruise",
                "order": 0,
                "profile_path": "/3xuBtKtQwAUnEAoMfYseNSFhRkq.jpg"
            },
            ...
        ],
        "crew": [
            {
                "credit_id": "55fdcfa7c3a368133400196b",
                "department": "Production",
                "gender": 2,
                "id": 500,
                "job": "Producer",
                "name": "Tom Cruise",
                "profile_path": "/3xuBtKtQwAUnEAoMfYseNSFhRkq.jpg"
            },
            ...
         ]
       },
    "release_dates": {
        "results": [
            {
                "iso_3166_1": "FR",
                "release_dates": [
                    {
                        "certification": "12",
                        "iso_639_1": "fr",
                        "note": "",
                        "release_date": "2016-10-19T00:00:00.000Z",
                        "type": 3
                    }
                ]
            },
            {
                "iso_3166_1": "CH",
                "release_dates": [
                    {
                        "certification": "",
                        "iso_639_1": "fr",
                        "release_date": "2016-10-19T00:00:00.000Z",
                        "type": 3
                    }
                ]
            },
            {
                "iso_3166_1": "GB",
                "release_dates": [
                    {
                        "certification": "12",
                        "iso_639_1": "en",
                        "note": "DVD",
                        "release_date": "2017-02-27T00:00:00.000Z",
                        "type": 5
                    },
                    {
                        "certification": "12A",
                        "iso_639_1": "",
                        "note": "",
                        "release_date": "2016-10-21T00:00:00.000Z",
                        "type": 3
                    }
                ]
            },
            {
                "iso_3166_1": "RU",
                "release_dates": [
                    {
                        "certification": "16+",
                        "iso_639_1": "",
                        "note": "",
                        "release_date": "2016-10-20T00:00:00.000Z",
                        "type": 1
                    }
                ]
            },
            {
                "iso_3166_1": "LT",
                "release_dates": [
                    {
                        "certification": "N-13",
                        "iso_639_1": "",
                        "note": "",
                        "release_date": "2016-10-21T00:00:00.000Z",
                        "type": 3
                    }
                ]
            },
            {
                "iso_3166_1": "US",
                "release_dates": [
                    {
                        "certification": "PG-13",
                        "iso_639_1": "",
                        "note": "",
                        "release_date": "2016-10-21T00:00:00.000Z",
                        "type": 3
                    },
                    {
                        "certification": "PG-13",
                        "iso_639_1": "en",
                        "note": "",
                        "release_date": "2017-01-31T00:00:00.000Z",
                        "type": 5
                    }
                ]
            }
        ]
    }
}
 */
@Serializable
data class MovieDetails(
    @SerialName("adult")
    val adult: Boolean = false,
    @SerialName("backdrop_path")
    val backdropPath: String? = null,
    @SerialName("belongs_to_collection")
    val belongsToCollection: CollectionInfo? = null,
    @SerialName("budget")
    val budget: Double,
    @SerialName("genres")
    val genres: List<Genre> = emptyList(),
    @SerialName("homepage")
    val homepage: String? = null,
    @SerialName("id")
    val tmdbId: Int,
    @SerialName("imdb_id")
    val imdbId: String? = null,
    @SerialName("original_language")
    val originalLanguage: String,
    @SerialName("original_title")
    val originalTitle: String,
    @SerialName("overview")
    val overview: String? = null,
    @SerialName("popularity")
    val popularity: Float,
    @SerialName("poster_path")
    val posterPath: String? = null,
    @SerialName("production_companies")
    val productionCompanies : List<ProductionCompany> = emptyList(),
    @SerialName("production_countries")
    val productionCountries : List<Country> = emptyList(),
    @SerialName("release_date")
    val releaseDate: String,
    @SerialName("revenue")
    val revenue: Int,
    @SerialName("runtime")
    val runtime: Int? = null,
    @SerialName("spoken_languages")
    val spokenLanguages: List<Language> = emptyList(),
    @SerialName("status")
    val status: String,
    @SerialName("tagline")
    val tagline: String? = null,
    @SerialName("title")
    val title: String,
    @SerialName("video")
    val video: Boolean,
    @SerialName("vote_average")
    val voteAverage: Double,
    @SerialName("vote_count")
    val voteCount: Int,
    @SerialName("credits")
    val credits: Credits,
    @SerialName("release_dates")
    val releaseDates: ReleaseDates
): TmdbResponse()

/*
{
    "birthday": "1962-07-03",
    "known_for_department": "Acting",
    "deathday": null,
    "id": 500,
    "name": "Tom Cruise",
    "also_known_as": [
        "Том Круз",
        "トム・クルーズ",
        "ทอม ครูซ",
        "湯姆·克魯斯",
        "톰 크루즈",
        "توم كروز",
        "Thomas Cruise Mapother IV",
        "Τομ Κρουζ",
        "טום קרוז",
        "Thomas \"Tom\" Cruise",
        "汤姆·克鲁斯",
        "Thomas \"Thotty\" Cruise"
    ],
    "gender": 2,
    "biography": "An American actor and filmmaker. He has been nominated for three Academy Awards and has won three Golden Globe Awards. He started his career at age 19 in the 1981 film Endless Love. After portraying supporting roles in Taps (1981) and The Outsiders (1983), his first leading role was in Risky Business, released in August 1983. Cruise became a full-fledged movie star after starring as Pete \"Maverick\" Mitchell in Top Gun (1986). He has since 1996 been well known for his role as secret agent Ethan Hunt in the Mission: Impossible film series. One of the biggest movie stars in Hollywood, Cruise has starred in many successful films, including The Color of Money (1986), Cocktail (1988), Rain Man (1988), Born on the Fourth of July (1989), Far and Away(1992), A Few Good Men (1992), The Firm (1993), Interview with the Vampire: The Vampire Chronicles (1994), Jerry Maguire (1996), Eyes Wide Shut (1999), Magnolia (1999), Vanilla Sky (2001), Minority Report (2002),The Last Samurai (2003), Collateral (2004), War of the Worlds (2005), Lions for Lambs (2007), Valkyrie (2008), Knight and Day (2010), Jack Reacher (2012), Oblivion (2013), and Edge of Tomorrow (2014). In 2012, Cruise was Hollywood's highest-paid actor. Fifteen of his films grossed over $100 million domestically; twenty-one have grossed in excess of $200 million worldwide. Cruise is known for his support for the Church of Scientology and its affiliated social programs.",
    "popularity": 20.156,
    "place_of_birth": "Syracuse, New York, USA",
    "profile_path": "/gHjTRoZbQZCg715q5mmz5XfTVSe.jpg",
    "adult": false,
    "imdb_id": "nm0000129",
    "homepage": "http://www.tomcruise.com",
    "images": {
        "profiles": [
            {
                "iso_639_1": null,
                "aspect_ratio": 0.66666666666667,
                "vote_count": 0,
                "height": 3000,
                "vote_average": 0,
                "file_path": "/cqAac7fwnXhaSkYCuLneUaLrABE.jpg",
                "width": 2000
            }, ...
         ]
    }
}
 */
@Serializable
data class PersonDetails(
    @SerialName("birthday")
    val birthday: String?,
    @SerialName("known_for_department")
    val knownForDepartment: String,
    @SerialName("deathday")
    val deathday: String? = null,
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String,
    @SerialName("also_known_as")
    val alsoKnownAs: List<String>,
    @SerialName("gender")
    val gender: Int,
    @SerialName("biography")
    val biography: String,
    @SerialName("popularity")
    val popularity: Float,
    @SerialName("place_of_birth")
    val placeOfBirth: String? = null,
    @SerialName("profile_path")
    val profilePath: String? = null,
    @SerialName("adult")
    val adult: Boolean,
    @SerialName("imdb_id")
    val imdbId: String? = null,
    @SerialName("homepage")
    val homepage: String? = null,
    @SerialName("images")
    val images: Images
):TmdbResponse()

@Serializable
data class MovieReference(
    @SerialName("id")
    val id: Int
)

@Serializable
data class Country(
    @SerialName("iso_3166_1")
    val code: String,
    @SerialName("name")
    val name: String
)

@Serializable
data class Language(
    @SerialName("iso_639_1")
    val code: String,
    @SerialName("name")
    val name: String
)

@Serializable
data class ProductionCompany(
    @SerialName("id")
    val id: Int,
    @SerialName("logo_path")
    val logoPath : String? = null,
    @SerialName("name")
    val name: String,
    @SerialName("origin_country")
    val originCountry: String
)

@Serializable
data class CollectionInfo(
    @SerialName("id")
    val tmdbCollectionId: Int,
    @SerialName("name")
    val name: String,
    @SerialName("poster_path")
    val posterPath: String? = null,
    @SerialName("backdrop_path")
    val backdropPath: String? = null
)

@Serializable
data class Genre(
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String
)

@Serializable
data class Cast(
    @SerialName("cast_id")
    val castId: Int,
    @SerialName("character")
    val character: String,
    @SerialName("credit_id")
    val creditId: String,
    @SerialName("gender")
    val gender: Int? = null,
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String,
    @SerialName("order")
    val order: Int,
    @SerialName("profile_path")
    val profilePath: String? = null
)

@Serializable
data class Crew(
    @SerialName("credit_id")
    val creditId: String,
    @SerialName("department")
    val department: String,
    @SerialName("gender")
    val gender: Int? = null,
    @SerialName("id")
    val id: Int,
    @SerialName("job")
    val job: String,
    @SerialName("name")
    val name: String,
    @SerialName("profile_path")
    val profilePath: String? = null
)

@Serializable
data class Credits(
    @SerialName("cast")
    val cast: List<Cast>,
    @SerialName("crew")
    val crew: List<Crew>
)

@Serializable
data class Release(
    @SerialName("certification")
    val certification: String,
    @SerialName("iso_639_1")
    val language: String?,
    @SerialName("release_date")
    val releaseDate: String,
    @SerialName("type")
    val type: Int,
    @SerialName("note")
    val note: String? = null
)

@Serializable
data class ReleasedCountry(
    @SerialName("iso_3166_1")
    val countryName: String,
    @SerialName("release_dates")
    val releases: List<Release>
)

@Serializable
data class ReleaseDates(
    @SerialName("results")
    val results: List<ReleasedCountry>
)

@Serializable
data class Images(
    @SerialName("profiles")
    val profiles: List<Profile>
)

@Serializable
data class Profile(
    @SerialName("iso_639_1")
    val language:String?,
    @SerialName("aspect_ratio")
    val aspectRatio: Double,
    @SerialName("vote_count")
    val voteCount: Int,
    @SerialName("vote_average")
    val voteAverage: Double,
    @SerialName("width")
    val width: Int,
    @SerialName("height")
    val height: Int,
    @SerialName("file_path")
    val filePath: String
)