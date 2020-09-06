package com.codingventures.movies.peopledb.tests.mapper

import com.codingventures.movies.mockdata.domain.mockPersonDetailsList
import com.codingventures.movies.peopledb.mapper.Statements
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe

class StatementsTests : ShouldSpec() {
    init {
        should("Create the people statement correctly") {
            val peopleStatament = Statements(mockPersonDetailsList).insertPeople

            val batchData = peopleStatament.batchData
            batchData.count() shouldBe 4

            val insertStatement = peopleStatament.insertStatement
            insertStatement shouldBe """INSERT INTO people (urn,external_urns,birthday,deathday,known_for_department,name,also_known_as,gender,biography,popularity,place_of_birth,profile_picture_path,homepage) VALUES ($1,$2,$3,$4,$5,$6,$7,$8,$9,$10,$11,$12,$13) ON CONFLICT (urn) DO NOTHING"""
        }
        should("Create the pictures statement correctly") {
            val picturesStatement = Statements(mockPersonDetailsList).insertPictures

            val batchData = picturesStatement.batchData
            batchData.count() shouldBe 8

            val insertStatement = picturesStatement.insertStatement
            insertStatement shouldBe """INSERT INTO pictures (id,person_urn,path,width,height) VALUES ($1,$2,$3,$4,$5) ON CONFLICT (id) DO NOTHING"""
        }
    }
}