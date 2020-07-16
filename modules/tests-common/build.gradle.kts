import dependencies.testContainersVersion
import dependencies.mockServerVersion
import dependencies.avro4kVersion
import dependencies.ktorVersion
import dependencies.coroutinesVersion
import dependencies.kotlinxSerializationVersion

dependencies {
    implementation(project(":modules:kafka-common"))
    implementation(project(":modules:movies-domain"))

    implementation("com.sksamuel.avro4k:avro4k-core:${avro4kVersion}")
    implementation("io.ktor:ktor-client-core:${ktorVersion}")
    implementation("io.ktor:ktor-client-cio:${ktorVersion}")
    implementation("io.ktor:ktor-client-serialization-jvm:$ktorVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${coroutinesVersion}")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime:${kotlinxSerializationVersion}")

    implementation("org.testcontainers:kafka:${testContainersVersion}")
    implementation("org.testcontainers:postgresql:${testContainersVersion}")
    implementation("org.testcontainers:testcontainers:${testContainersVersion}")
    implementation("org.mock-server:mockserver-client-java:${mockServerVersion}")
}