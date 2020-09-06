import dependencies.kotlinxSerializationVersion
import dependencies.coroutinesVersion

buildscript {
    dependencies {
        classpath("org.postgresql:postgresql:42.2.12")
    }
}

plugins {
    id("com.google.cloud.tools.jib")
    id("org.flywaydb.flyway")
}

dependencies {
    implementation(project(":modules:movies-domain"))
    implementation(project(":modules:kafka-common"))

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime:$kotlinxSerializationVersion")

}