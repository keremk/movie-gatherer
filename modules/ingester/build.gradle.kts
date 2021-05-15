import dependencies.avroSerializerVersion
import dependencies.hopliteVersion
import dependencies.avro4kVersion
import dependencies.coroutinesVersion
import dependencies.kotlinxSerializationVersion
import dependencies.ktorVersion
import dependencies.kafkaVersion
import dependencies.kotlinLogVersion
import dependencies.simpleLogVersion
import dependencies.kotestVersion
import dependencies.testContainersVersion

plugins {
    id("com.google.cloud.tools.jib")
}

dependencies {
    implementation(project(":modules:movies-domain"))
    implementation(project(":modules:kafka-common"))

    implementation("io.confluent:kafka-avro-serializer:$avroSerializerVersion")
    implementation("com.sksamuel.hoplite:hoplite-core:$hopliteVersion")
    implementation("com.sksamuel.hoplite:hoplite-yaml:$hopliteVersion")
    implementation("com.github.avro-kotlin.avro4k:avro4k-core:$avro4kVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$kotlinxSerializationVersion")
    implementation("io.ktor:ktor-client-core:$ktorVersion")
    implementation("io.ktor:ktor-client-cio:$ktorVersion")
    implementation("io.ktor:ktor-client-serialization-jvm:$ktorVersion")
    implementation("org.apache.kafka:kafka-clients:$kafkaVersion")
    implementation("io.github.microutils:kotlin-logging:$kotlinLogVersion")
    implementation("org.slf4j:slf4j-simple:$simpleLogVersion")

    testImplementation(project(":modules:tests-common"))
    testImplementation("io.ktor:ktor-client-mock:$ktorVersion")
    testImplementation("io.ktor:ktor-client-mock-jvm:$ktorVersion")
    testImplementation("io.kotest:kotest-runner-console-jvm:$kotestVersion")
    testImplementation("org.testcontainers:testcontainers:$testContainersVersion")
}
