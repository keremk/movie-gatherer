package com.codingventures.movies.peopledb.mapper

import com.codingventures.movies.domain.PersonDetails
import com.codingventures.movies.domain.Picture
import com.codingventures.movies.peopledb.data.Picture as PictureDb
import com.codingventures.movies.domain.Urn
import com.codingventures.movies.peopledb.data.People

object Mappers {
    fun mapPerson(personDetails: PersonDetails) =
        People(
            urn = personDetails.personUrn.toString(),
            externalUrns = mapExternalUrns(personDetails.externalUrns),
            birthDay = personDetails.birthday,
            deathday = personDetails.deathday,
            knownForDepartment = personDetails.knownForDepartment,
            name = personDetails.name,
            alsoKnownAs = mapAlsoKnownAs(personDetails.alsoKnownAs),
            gender = personDetails.gender.toString(),
            biography = personDetails.biography,
            popularity = personDetails.popularity,
            placeOfBirth = personDetails.placeOfBirth,
            profilePicturePath = personDetails.profilePicturePath,
            homepage = personDetails.homepage
        )

    fun mapPictures(personUrn: Urn, pictures: List<Picture>) =
        pictures.map {
            PictureDb(
                id = -1,
                personUrn = personUrn.toString(),
                path = it.path,
                width = it.width,
                height = it.height
            )
        }

    private fun mapExternalUrns(urns: List<Urn>) = urns.map { it.toString() }.toTypedArray()
    private fun mapAlsoKnownAs(names: List<String>) = names.toTypedArray()
}