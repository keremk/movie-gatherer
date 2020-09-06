package com.codingventures.movies.moviesdb.tests.utils

import com.codingventures.movies.containers.TestPostgreSQL
import com.codingventures.movies.dbcommon.config.PgConfigProvider
import org.flywaydb.core.Flyway

fun initializeTargetDb(): PgConfigProvider {
    val db = TestPostgreSQL.initialize()

    val flyway = Flyway.configure().dataSource(db.jdbcUrl, db.username, db.password).load()
    flyway.clean()
    flyway.migrate()

    return PgConfigProvider(
        port = db.dbMappedPort,
        host = db.host,
        database = db.databaseName,
        user = db.username,
        password = db.password,
        maxPoolSize = 16
    )
}

