package com.codingventures.movies.peopledb.tests.integration

import com.codingventures.movies.dbcommon.config.PgConfigProvider
import com.codingventures.movies.dbcommon.db.DBService
import com.codingventures.movies.dbcommon.utils.connectToDb
import com.codingventures.movies.mockdata.domain.mockPersonDetailsList
import com.codingventures.movies.peopledb.mapper.Statements
import com.codingventures.movies.peopledb.mapper.prepareStatements
import com.codingventures.movies.peopledb.tests.utils.initializeTargetDb
import io.kotest.assertions.throwables.shouldNotThrow
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.Spec
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import io.vertx.kotlin.sqlclient.*
import io.vertx.pgclient.PgException
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class MoviesDbTests : ShouldSpec() {
    private lateinit var pgConfigProvider: PgConfigProvider

    override fun isolationMode(): IsolationMode? = IsolationMode.InstancePerTest

    override fun beforeSpec(spec: Spec) {
        super.beforeSpec(spec)
        pgConfigProvider = initializeTargetDb()
    }

    init {
        should("Persist single movie to DB") {
            val pgClient = connectToDb(pgConfigProvider)
            val dbService = DBService.initialize(pgClient)
            val statements = Statements(listOf(mockPersonDetailsList[1]))

            val results = dbService.insert(listOf(statements.insertPeople))

            results.count() shouldBe 1
            results[0].rowCount() shouldBe 1

            val rowSet = pgClient.query("SELECT * FROM people WHERE urn = 'cv:person:31'").executeAwait()
            rowSet.rowCount() shouldBe 1

            for (row in rowSet) {
                row.getString(0) shouldBe "cv:person:31"
                row.getStringArray(1) shouldBe arrayOf("imdb:person:nm0000158")
                row.getString(5) shouldBe "Tom Hanks"
                row.getStringArray(6) shouldBe arrayOf(
                    "Thomas Jeffrey Hanks",
                    "Том Хэнкс",
                    "توم هانكس",
                    "トム・ハンクス"
                )
            }
            pgClient.close()
        }

        should("Persist the same movie only once to DB") {
            val pgClient = connectToDb(pgConfigProvider)
            val dbService = DBService.initialize(pgClient)
            val statements = Statements(listOf(mockPersonDetailsList[1]))

            val results = dbService.insert(listOf(statements.insertPeople))

            results.count() shouldBe 1
            results[0].rowCount() shouldBe 1

            shouldNotThrow<PgException> {
                val secondResults = dbService.insert(listOf(statements.insertPeople))

                secondResults.count() shouldBe 1
                secondResults[0].rowCount() shouldBe 0
            }

            val rowSet = pgClient.query("SELECT * FROM people WHERE urn = 'cv:person:31'").executeAwait()
            rowSet.rowCount() shouldBe 1
            pgClient.close()
        }

        should("Persist the full movie details to its corresponding tables") {
            val pgClient = connectToDb(pgConfigProvider)
            val dbService = DBService.initialize(pgClient)
            val statements = prepareStatements(mockPersonDetailsList)

            val results = dbService.insert(statements)
            results.count() shouldBe 2

            val movies = pgClient.query("SELECT * FROM people").executeAwait()
            movies.rowCount() shouldBe 4
            val releases = pgClient.query("SELECT * FROM pictures").executeAwait()
            releases.rowCount() shouldBe 8

            pgClient.close()
        }
    }
}