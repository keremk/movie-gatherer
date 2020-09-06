package com.codingventures.movies.moviesdb.data

import com.codingventures.annotations.Column
import com.codingventures.annotations.Table

@Table("casting", primaryKey = "id")
data class Cast(
    @Column("id", exclude = true)
    val id: Int,
    @Column("movie_urn")
    val movieUrn: String,
    @Column("person_urn")
    val personUrn: String,
    @Column("character_name")
    val characterName: String,
    @Column("ordinal")
    val ordinal: Int
)
