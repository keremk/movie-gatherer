package com.codingventures.movies.dbcommon.utils

import com.codingventures.movies.dbcommon.config.PgConfigProvider
import io.vertx.kotlin.pgclient.pgConnectOptionsOf
import io.vertx.kotlin.sqlclient.poolOptionsOf
import io.vertx.pgclient.PgPool

fun connectToDb(pgConfigProvider: PgConfigProvider): PgPool {
    val connectOptions = pgConnectOptionsOf(
        port = pgConfigProvider.port,
        host = pgConfigProvider.host,
        database = pgConfigProvider.database,
        user = pgConfigProvider.user,
        password = pgConfigProvider.password
    )
    val poolOptions = poolOptionsOf(maxSize = 5)
    return PgPool.pool(connectOptions, poolOptions)
}