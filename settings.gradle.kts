/*
 * This file was generated by the Gradle 'init' task.
 *
 * The settings file is used to specify which projects to include in your build.
 *
 * Detailed information about configuring a multi-project build in Gradle can be found
 * in the user manual at https://docs.gradle.org/6.3/userguide/multi_project_builds.html
 */

rootProject.name = "movies"

include(
    ":modules:moviecmd",
    ":modules:movies-domain",
    ":modules:ingester",
    ":modules:moviesdb",
    ":modules:peopledb",
    ":modules:kafka-common",
    ":modules:ingester-integration-tests",
    ":modules:tests-common",
    ":modules:db-common",
    ":modules:db-annotation",
    ":modules:db-processor"
)

// Microservices
project(":modules:ingester").name = "ingester"
project(":modules:moviesdb").name = "moviesdb"
project(":modules:peopledb").name = "peopledb"
project(":modules:ingester-integration-tests").name = "ingester-integration-tests"

// Common Libraries
project(":modules:movies-domain").name = "movies-domain"
project(":modules:kafka-common").name = "kafka-common"
project(":modules:tests-common").name = "tests-common"
project(":modules:db-common").name = "db-common"

// Annotations & Processor
project(":modules:db-annotation").name = "db-annotation"
project(":modules:db-processor").name = "db-processor"

// Helpers
project(":modules:moviecmd").name = "moviecmd"

pluginManagement {
    val kotlinVersion: String by settings

    plugins {
        application
        kotlin("jvm") version kotlinVersion
        id("org.jetbrains.kotlin.plugin.serialization") version kotlinVersion
    }
}
