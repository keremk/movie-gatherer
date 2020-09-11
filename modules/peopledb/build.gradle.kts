import dependencies.kotlinxSerializationVersion
import dependencies.coroutinesVersion
import dependencies.vertxVersion
import dependencies.testContainersVersion
import dependencies.kotestVersion
import dependencies.flywayVersion
import dependencies.avro4kVersion
import dependencies.avroSerializerVersion
import dependencies.kafkaVersion
import dependencies.postgresVersion
import dependencies.hopliteVersion

buildscript {
    dependencies {
        classpath("org.postgresql:postgresql:42.2.12")
    }
}

plugins {
    id("com.google.cloud.tools.jib")
    id("org.flywaydb.flyway")
    kotlin("kapt")
}

dependencies {
    implementation(project(":modules:movies-domain"))
    implementation(project(":modules:kafka-common"))
    implementation(project(":modules:db-common"))
    implementation(project(":modules:db-annotation"))
    kapt(project(":modules:db-processor"))

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime:$kotlinxSerializationVersion")
    implementation("io.vertx:vertx-lang-kotlin:$vertxVersion")
    implementation("io.vertx:vertx-pg-client:$vertxVersion")
    implementation("io.vertx:vertx-lang-kotlin-coroutines:$vertxVersion")
    implementation("com.sksamuel.hoplite:hoplite-core:$hopliteVersion")
    implementation("com.sksamuel.hoplite:hoplite-yaml:$hopliteVersion")

    testImplementation("io.confluent:kafka-avro-serializer:${avroSerializerVersion}")
    testImplementation("org.apache.kafka:kafka-clients:${kafkaVersion}")
    testImplementation("com.sksamuel.avro4k:avro4k-core:${avro4kVersion}")

    testImplementation(project(":modules:tests-common"))
    testImplementation("org.flywaydb:flyway-core:${flywayVersion}")
    testImplementation("org.postgresql:postgresql:$postgresVersion")
    testImplementation("org.testcontainers:kafka:${testContainersVersion}")
    testImplementation("org.testcontainers:postgresql:${testContainersVersion}")
    testImplementation("org.testcontainers:testcontainers:${testContainersVersion}")
    testImplementation("io.kotest:kotest-runner-console-jvm:${kotestVersion}")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutinesVersion")
}