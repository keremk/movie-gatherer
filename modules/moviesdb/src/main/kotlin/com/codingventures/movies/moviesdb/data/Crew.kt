package com.codingventures.movies.moviesdb.data

import com.codingventures.annotations.Column
import com.codingventures.annotations.Table

@Table("crew", primaryKey = "id")
data class Crew(
    @Column("id", exclude = true)
    val id: Int,
    @Column("movie_urn")
    val movieUrn: String,
    @Column("person_urn")
    val personUrn: String,
    @Column("job_role")
    val jobRole: String,
    @Column("department")
    val department: String
)