package com.codingventures.movies.moviesdb.tests.integration

import com.codingventures.movies.dbcommon.config.PgConfigProvider
import com.codingventures.movies.dbcommon.db.DBService
import com.codingventures.movies.dbcommon.utils.connectToDb
import com.codingventures.movies.mockdata.domain.mockMovies
import com.codingventures.movies.moviesdb.mapper.Statements
import com.codingventures.movies.moviesdb.mapper.prepareStatements
import com.codingventures.movies.moviesdb.tests.utils.initializeTargetDb
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
            val statements = Statements(listOf(mockMovies[0]))

            val results = dbService.insert(listOf(statements.insertMovies))

            results.count() shouldBe 1
            results[0].rowCount() shouldBe 1

            val rowSet = pgClient.query("SELECT * FROM movies WHERE urn = 'cv:movie:12345'").executeAwait()
            rowSet.rowCount() shouldBe 1

            for (row in rowSet) {
                row.getString(0) shouldBe "cv:movie:12345"
                row.getStringArray(1) shouldBe arrayOf("imdb:movie:1234")
                row.getString(2) shouldBe "Spenser Confidential"
                row.getIntegerArray(5) shouldBe arrayOf(35, 28)
            }
            pgClient.close()
        }

        should("Persist the same movie only once to DB") {
            val pgClient = connectToDb(pgConfigProvider)
            val dbService = DBService.initialize(pgClient)
            val statements = Statements(listOf(mockMovies[0]))

            val results = dbService.insert(listOf(statements.insertMovies))

            results.count() shouldBe 1
            results[0].rowCount() shouldBe 1

            shouldNotThrow<PgException> {
                val secondResults = dbService.insert(listOf(statements.insertMovies))

                secondResults.count() shouldBe 1
                secondResults[0].rowCount() shouldBe 0
            }

            val rowSet = pgClient.query("SELECT * FROM movies WHERE urn = 'cv:movie:12345'").executeAwait()
            rowSet.rowCount() shouldBe 1
            pgClient.close()
        }

        should("Persist the full movie details to its corresponding tables") {
            val pgClient = connectToDb(pgConfigProvider)
            val dbService = DBService.initialize(pgClient)
            val statements = prepareStatements(mockMovies)

            val results = dbService.insert(statements)
            results.count() shouldBe 4

            val movies = pgClient.query("SELECT * FROM movies").executeAwait()
            movies.rowCount() shouldBe 3
            val releases = pgClient.query("SELECT * FROM releases").executeAwait()
            releases.rowCount() shouldBe 8
            val cast = pgClient.query("SELECT * FROM casting").executeAwait()
            cast.rowCount() shouldBe 9
            val crew = pgClient.query("SELECT * FROM crew").executeAwait()
            crew.rowCount() shouldBe 7
            pgClient.close()
        }
    }
}