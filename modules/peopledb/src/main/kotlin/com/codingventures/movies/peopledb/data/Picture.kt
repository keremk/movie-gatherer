package com.codingventures.movies.peopledb.data

import com.codingventures.annotations.Column
import com.codingventures.annotations.Table
import kotlinx.serialization.SerialName

@Table("pictures", primaryKey = "id")
data class Picture(
    @Column("id")
    val id: Int,
    @Column("person_urn")
    val personUrn: String,
    @Column("path")
    val path: String,
    @Column("width")
    val width: Int,
    @Column("height")
    val height: Int
)