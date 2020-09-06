package com.codingventures.movies.peopledb.data

import com.codingventures.annotations.Column
import com.codingventures.annotations.Table
import com.codingventures.movies.domain.Gender
import kotlinx.serialization.SerialName
import java.time.LocalDate

@Table("people", primaryKey = "urn")
data class People(
    @Column("urn")
    val urn: String,
    @Column("external_urns")
    val externalUrns: Array<String>,
    @Column("birthday")
    val birthDay: LocalDate?,
    @Column("deathday")
    val deathday: LocalDate?,
    @Column("known_for_department")
    val knownForDepartment: String,
    @Column("name")
    val name: String,
    @Column("also_known_as")
    val alsoKnownAs: Array<String>,
    @Column("gender")
    val gender: String,
    @Column("biography")
    val biography: String,
    @Column("popularity")
    val popularity: Double,
    @Column("place_of_birth")
    val placeOfBirth: String?,
    @Column("profile_picture_path")
    val profilePicturePath: String?,
    @Column("homepage")
    val homepage: String?
)
