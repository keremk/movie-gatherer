import dependencies.mockServerVersion
import dependencies.testContainersVersion
import dependencies.coroutinesVersion
import dependencies.ktorVersion
import dependencies.kotlinxSerializationVersion
import dependencies.kafkaVersion
import dependencies.avroSerializerVersion
import dependencies.avro4kVersion

dependencies {
    implementation(project(":modules:ingester"))
    implementation(project(":modules:tests-common"))
    implementation(project(":modules:kafka-common"))
    implementation(project(":modules:movies-domain"))

    implementation("io.confluent:kafka-avro-serializer:${avroSerializerVersion}")
    implementation("org.apache.kafka:kafka-clients:${kafkaVersion}")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${coroutinesVersion}")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime:${kotlinxSerializationVersion}")
    implementation("io.ktor:ktor-client-core:${ktorVersion}")
    implementation("io.ktor:ktor-client-cio:${ktorVersion}")
    implementation("io.ktor:ktor-client-serialization-jvm:${ktorVersion}")
    implementation("com.sksamuel.avro4k:avro4k-core:${avro4kVersion}")


    testImplementation("org.testcontainers:mockserver:${testContainersVersion}")
    testImplementation("org.testcontainers:kafka:${testContainersVersion}")
    testImplementation("org.testcontainers:postgresql:${testContainersVersion}")
    testImplementation("org.testcontainers:testcontainers:${testContainersVersion}")
    testImplementation("org.mock-server:mockserver-client-java:${mockServerVersion}")
}