package com.codingventures.movies.containers

import org.testcontainers.containers.KafkaContainer
import org.testcontainers.containers.PostgreSQLContainer
import java.util.concurrent.atomic.AtomicBoolean

object TestPostgreSQL {
    const val version = "12.4"

    // https://github.com/testcontainers/testcontainers-java/issues/238
    class TestPostgreSQLContainer(dockerImageName: String?) :
        PostgreSQLContainer<TestPostgreSQLContainer?>(dockerImageName) {
        val dbMappedPort by lazy { getMappedPort(POSTGRESQL_PORT) }
    }

    private val postgres: TestPostgreSQLContainer by lazy {
        TestPostgreSQLContainer("postgres:$version")
    }
    private val initialized = AtomicBoolean(false)

    fun initialize(): TestPostgreSQLContainer {
        if (initialized.compareAndSet(false, true)) {
            postgres.start()
        }
        return postgres
    }
}


