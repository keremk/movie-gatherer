import dependencies.avroSerializerVersion
import dependencies.hopliteVersion
import dependencies.avro4kVersion
import dependencies.coroutinesVersion
import dependencies.kotlinxSerializationVersion
import dependencies.ktorVersion
import dependencies.kafkaVersion
import dependencies.kotlinLogVersion
import dependencies.simpleLogVersion
import dependencies.arrowVersion

plugins {
    kotlin("kapt")
}

dependencies {
    implementation(project(":modules:movies-domain"))
    implementation(project(":modules:kafka-common"))

    implementation("io.arrow-kt:arrow-core:$arrowVersion")
    implementation("io.arrow-kt:arrow-syntax:$arrowVersion")
//    kapt("io.arrow-kt:arrow-meta:$arrowVersion")

    implementation("io.confluent:kafka-avro-serializer:$avroSerializerVersion")
    implementation("com.sksamuel.hoplite:hoplite-core:$hopliteVersion")
    implementation("com.sksamuel.hoplite:hoplite-yaml:$hopliteVersion")
    implementation("com.sksamuel.avro4k:avro4k-core:$avro4kVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime:$kotlinxSerializationVersion")
    implementation("io.ktor:ktor-client-core:$ktorVersion")
    implementation("io.ktor:ktor-client-cio:$ktorVersion")
    implementation("io.ktor:ktor-client-serialization-jvm:$ktorVersion")
    implementation("org.apache.kafka:kafka-clients:$kafkaVersion")
    implementation("io.github.microutils:kotlin-logging:$kotlinLogVersion")
    implementation("org.slf4j:slf4j-simple:$simpleLogVersion")

    testImplementation("io.ktor:ktor-client-mock:$ktorVersion")
    testImplementation("io.ktor:ktor-client-mock-jvm:$ktorVersion")
}