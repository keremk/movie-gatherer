package com.codingventures.movies.moviesdb.tests.mapper

import com.codingventures.movies.mockdata.domain.mockMovies
import com.codingventures.movies.moviesdb.mapper.Statements
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe

class StatementsTests : ShouldSpec() {
    init {
        should("Create the movies statement correctly") {
            val moviesStatement = Statements(mockMovies).insertMovies

            val batchData = moviesStatement.batchData
            batchData.count() shouldBe 3

            val insertStatement = moviesStatement.insertStatement
            insertStatement shouldBe """INSERT INTO movies (urn,external_urns,title,overview,genres,original_title,original_language,spoken_languages,backdrop_path,poster_path,homepage,runtime,budget,revenue,release_date,production_status,production_countries,production_company_urns,popularity,vote_average,vote_count) VALUES ($1,$2,$3,$4,$5,$6,$7,$8,$9,$10,$11,$12,$13,$14,$15,$16,$17,$18,$19,$20,$21) ON CONFLICT (urn) DO NOTHING"""
        }
        should("Create the releases statement correctly") {
            val releasesStatement = Statements(mockMovies).insertReleases

            val batchData = releasesStatement.batchData
            batchData.count() shouldBe 8

            val insertStatement = releasesStatement.insertStatement
            insertStatement shouldBe """INSERT INTO releases (movie_urn,country_code,release_date,spoken_language,certification,release_type) VALUES ($1,$2,$3,$4,$5,$6) ON CONFLICT (id) DO NOTHING"""
        }
        should("Create the casting statement correctly") {
            val castingStatement = Statements(mockMovies).insertCast

            val batchData = castingStatement.batchData
            batchData.count() shouldBe 9

            val insertStatement = castingStatement.insertStatement
            insertStatement shouldBe """INSERT INTO casting (movie_urn,person_urn,character_name,ordinal) VALUES ($1,$2,$3,$4) ON CONFLICT (id) DO NOTHING"""
        }
        should("Create the crew statement correctly") {
            val crewStatement = Statements(mockMovies).insertCrew

            val batchData = crewStatement.batchData
            batchData.count() shouldBe 7

            val insertStatement = crewStatement.insertStatement
            insertStatement shouldBe """INSERT INTO crew (movie_urn,person_urn,job_role,department) VALUES ($1,$2,$3,$4) ON CONFLICT (id) DO NOTHING"""
        }
    }
}