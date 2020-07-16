@file:UseSerializers(DateSerializer::class, UrnSerializer::class)

package com.codingventures.movies.domain

import com.codingventures.movies.serializers.DateSerializer
import com.codingventures.movies.serializers.UrnSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import java.time.LocalDate

sealed class MovieIndustryData

@Serializable
data class MovieDetails(
    @SerialName("movie_urn")
    val movieUrn: Urn,
    @SerialName("external_urns")
    val externalUrns: List<Urn>,

    @SerialName("title")
    val title: String,
    @SerialName("tagline")
    val tagline: String,
    @SerialName("overview")
    val overview: String,
    @SerialName("genres")
    val genres: List<Genre> = emptyList(),
    @SerialName("original_title")
    val originalTitle: String,
    @SerialName("original_language")
    val originalLanguage: LanguageCode,
    @SerialName("spoken_languages")
    val spokenLanguages: List<LanguageCode> = emptyList(),
    @SerialName("backdrop_path")
    val backdropPath: String? = null,
    @SerialName("poster_path")
    val posterPath: String? = null,
    @SerialName("homepage")
    val homepage: String? = null,
    @SerialName("runtime")
    val runtime: Int,

    @SerialName("budget")
    val budget: Double,
    @SerialName("revenue")
    val revenue: Double,

    @SerialName("release_date")
    val releaseDate: LocalDate,
    @SerialName("releases")
    val releases: Map<String, List<Release>>,
    @SerialName("production_status")
    val productionStatus: ProductionStatus,
    @SerialName("production_companies")
    val productionCompanies : List<Company> = emptyList(),
    @SerialName("production_countries")
    val productionCountries : List<CountryCode> = emptyList(),

    @SerialName("cast")
    val cast: List<Cast>,
    @SerialName("crew")
    val crew: List<Crew>,

    @SerialName("popularity")
    val popularity: Double,
    @SerialName("vote_average")
    val voteAverage: Double,
    @SerialName("vote_count")
    val voteCount: Int
) : MovieIndustryData()


@Serializable
data class PersonDetails(
    @SerialName("person_urn")
    val personUrn: Urn,
    @SerialName("external_urns")
    val externalUrns: List<Urn>,

    @SerialName("birthday")
    val birthday: LocalDate? = null,
    @SerialName("deathday")
    val deathday: LocalDate? = null,
    @SerialName("known_for_department")
    val knownForDepartment: String,

    @SerialName("name")
    val name: String,
    @SerialName("also_known_as")
    val alsoKnownAs: List<String>,

    @SerialName("gender")
    val gender: Gender,
    @SerialName("biography")
    val biography: String,
    @SerialName("popularity")
    val popularity: Double,
    @SerialName("place_of_birth")
    val placeOfBirth: String? = null,
    @SerialName("profile_picture_path")
    val profilePicturePath: String? = null,
    @SerialName("homepage")
    val homepage: String? = null,

    @SerialName("pictures")
    val pictures: List<Picture> = emptyList()
): MovieIndustryData()

@Serializable
data class Genre(
    @SerialName("urn")
    val id: Int,
    @SerialName("name")
    val name: String
)

@Serializable
enum class ProductionStatus(val status: String) {
    Rumored("Rumored"),
    Planned("Planned"),
    InProduction("In Production"),
    PostProduction("Post Production"),
    Released("Released"),
    Canceled("Canceled"),
    Unspecified("Unspecified");

    companion object {
        private val values = ProductionStatus.values()
        fun fromString(value: String): ProductionStatus {
            val status = values.firstOrNull { it.status == value }
            return status ?: Unspecified
        }
    }
}

@Serializable
data class Cast(
    @SerialName("actor_urn")
    val urn: Urn,
    @SerialName("character")
    val character: String,
    @SerialName("order")
    val order: Int
)

@Serializable
data class Crew(
    @SerialName("crew_urn")
    val urn: Urn,
    @SerialName("job_role")
    val jobRole: String,
    @SerialName("department")
    val department: String
)

@Serializable
data class Company(
    @SerialName("company_urn")
    val urn: Urn
)

@Serializable
data class Release(
    @SerialName("date")
    val date: LocalDate,
    @SerialName("language")
    val language: LanguageCode,
    @SerialName("certification")
    val certification: Certification,
    @SerialName("release_type")
    val releaseType: ReleaseType
)

@Serializable
enum class ReleaseType(val type:Int) {
    Premiere(1),
    TheatricalLimited (2),
    Theatrical(3),
    Digital(4),
    Physical(5),
    TV(6),
    Unspecified(0);

    companion object {
        private val values = values()
        fun fromInt(value: Int): ReleaseType {
            val type = values.firstOrNull { it.type == value }
            return type ?: ReleaseType.Unspecified
        }
    }
}

@Serializable
enum class Gender(val desc: String) {
    Male("Male"),
    Female("Female"),
    Unstated("Unstated");

    companion object {
        private val values = Gender.values()
        fun fromString(value: String): Gender {
            val desc = values.firstOrNull { it.desc == value }
            return desc ?: Unstated
        }
    }
}

@Serializable
data class Picture(
    @SerialName("path")
    val path: String,
    @SerialName("width")
    val width: Int,
    @SerialName("height")
    val height: Int
)