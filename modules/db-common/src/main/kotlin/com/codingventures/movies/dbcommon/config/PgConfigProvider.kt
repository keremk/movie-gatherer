package com.codingventures.movies.dbcommon.config

import com.sksamuel.hoplite.ConfigLoader

data class PgConfigProvider(
    val port: Int,
    val host: String,
    val database: String,
    val user: String,
    val password: String,
    val maxPoolSize: Int
) {

    companion object {
        fun default(): PgConfigProvider = ConfigLoader().loadConfigOrThrow<PgConfigProvider>("/pg-config.yaml")
    }
}