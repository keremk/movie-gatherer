package com.codingventures.movies.peopledb.mapper

import com.codingventures.movies.dbcommon.pipeline.StatementDeclaration
import com.codingventures.movies.domain.PersonDetails
import com.codingventures.movies.peopledb.data.PeopleDbHelper
import com.codingventures.movies.peopledb.data.PictureDbHelper
import com.codingventures.movies.peopledb.data.valuesAsList

fun prepareStatements(people: List<PersonDetails>): List<StatementDeclaration> {
    val statements = Statements(people)

    return listOf(
        statements.insertPeople,
        statements.insertPictures
    )
}

class Statements(val people: List<PersonDetails>) {
    val insertPeople by lazy {
        StatementDeclaration(
            insertStatement = PeopleDbHelper.generateInsertStatement(),
            batchData = people
                .map { Mappers.mapPerson(it) }
                .map { it.valuesAsList() as List<Any> }
        )
    }

    val insertPictures by lazy {
        StatementDeclaration(
            insertStatement = PictureDbHelper.generateInsertStatement(),
            batchData = people
                .map { Pair(it.personUrn, it.pictures) }
                .map { Mappers.mapPictures(it.first, it.second) }
                .flatten()
                .map { it.valuesAsList() as List<Any> }
        )
    }
}