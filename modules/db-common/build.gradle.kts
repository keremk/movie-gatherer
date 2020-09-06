import dependencies.coroutinesVersion
import dependencies.kotlinxSerializationVersion
import dependencies.avro4kVersion
import dependencies.avroSerializerVersion
import dependencies.hopliteVersion
import dependencies.vertxVersion
import dependencies.kotestVersion

dependencies {
    implementation(project(":modules:movies-domain"))
    implementation(project(":modules:kafka-common"))

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${coroutinesVersion}")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime:${kotlinxSerializationVersion}")
    implementation("io.confluent:kafka-avro-serializer:${avroSerializerVersion}")
    implementation("com.sksamuel.hoplite:hoplite-core:${hopliteVersion}")
    implementation("com.sksamuel.hoplite:hoplite-yaml:${hopliteVersion}")
    implementation("com.sksamuel.avro4k:avro4k-core:${avro4kVersion}")
    implementation("io.vertx:vertx-lang-kotlin:${vertxVersion}")
    implementation("io.vertx:vertx-pg-client:${vertxVersion}")
    implementation("io.vertx:vertx-lang-kotlin-coroutines:$vertxVersion")

    testImplementation(project(":modules:tests-common"))
    testImplementation("io.kotest:kotest-runner-console-jvm:${kotestVersion}")
}