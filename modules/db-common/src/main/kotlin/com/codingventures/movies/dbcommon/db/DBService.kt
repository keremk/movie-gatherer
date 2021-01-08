package com.codingventures.movies.dbcommon.db

import com.codingventures.movies.dbcommon.pipeline.StatementDeclaration
import io.vertx.kotlin.sqlclient.beginAwait
import io.vertx.kotlin.sqlclient.commitAwait
import io.vertx.kotlin.sqlclient.executeBatchAwait
import io.vertx.pgclient.PgPool
import io.vertx.sqlclient.Tuple
import io.vertx.sqlclient.Row
import io.vertx.sqlclient.RowSet
import io.vertx.sqlclient.Transaction
import mu.KotlinLogging
import java.lang.IllegalStateException

private val logger = KotlinLogging.logger {}

class DBService(private val pgClient: PgPool) {

    suspend fun insert(statements: List<StatementDeclaration>): List<RowSet<Row>> {
        if (statements.isEmpty()) {
            logger.warn { "No statements are provided to persist" }
            return emptyList()
        }

        val tx = pgClient.beginAwait()
        val results = statements
            .filter { it.batchData.isNotEmpty() }
            .map { executeStatement(tx, it) }
        tx.commitAwait()

        return results
    }

    private suspend fun executeStatement(tx: Transaction, statement: StatementDeclaration): RowSet<Row> {
        val batch = statement.batchData.map { Tuple.tuple(it) }
        val result = tx.preparedQuery(statement.insertStatement).executeBatchAwait(batch)
        if (result == null) {
            logger.error { "${statement.batchData} can not be inserted" }
            throw IllegalStateException()
        } else {
            logger.info { "${result.value()} is inserted" }
        }
        return result
    }

    companion object {
        fun initialize(pgClient: PgPool): DBService {
            return DBService(pgClient = pgClient)
        }
    }
}