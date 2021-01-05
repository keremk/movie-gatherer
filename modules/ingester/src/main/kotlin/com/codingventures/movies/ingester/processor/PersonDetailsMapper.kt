package com.codingventures.movies.ingester.processor

import com.codingventures.movies.domain.*
import com.codingventures.movies.ingester.remote.tmdb.response.Images
import com.codingventures.movies.ingester.remote.tmdb.response.PersonDetails as PersonDetailsResponse
import java.time.LocalDate
import java.time.format.DateTimeFormatter

fun mapPersonResponseToPersonDetails(response: PersonDetailsResponse): PersonDetails {
    return PersonDetails(
        personUrn = createPersonUrn(response.id),
        externalUrns = listOfNotNull(
            createTMDBPersonUrn(response.id),
            createIMDBPersonUrn(response.imdbId)
        ),
        birthday = response.birthday?.let { LocalDate.parse(response.birthday, DateTimeFormatter.ISO_LOCAL_DATE)},
        deathday = response.deathday?.let { LocalDate.parse(it, DateTimeFormatter.ISO_LOCAL_DATE) },
        knownForDepartment = response.knownForDepartment,
        name = response.name,
        alsoKnownAs = response.alsoKnownAs,
        gender = mapGender(response.gender),
        biography = response.biography,
        popularity = response.popularity.toDouble(),
        placeOfBirth = response.placeOfBirth?.let { it },
        profilePicturePath = response.profilePath,
        homepage = response.homepage,
        pictures = mapPictures(response.images).take(5)
    )
}

fun mapGender(genderId: Int): Gender = when (genderId) {
    1 -> Gender.Female
    2 -> Gender.Male
    else -> Gender.Unstated
}

fun mapPictures(images: Images): List<Picture> {
    return images.profiles.map { image ->
        Picture(
            path = image.filePath,
            width = image.width,
            height = image.height
        )
    }
}