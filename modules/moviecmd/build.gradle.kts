import dependencies.avro4kVersion
import dependencies.cliktVersion
import dependencies.coroutinesVersion
import dependencies.ktorVersion
import dependencies.kotlinxSerializationVersion

plugins {
    application
}

dependencies {
    implementation(project(":modules:movies-domain"))
    implementation(project(":modules:ingester"))

    implementation("io.ktor:ktor-client-core:${ktorVersion}")
    implementation("io.ktor:ktor-client-cio:${ktorVersion}")
    implementation("io.ktor:ktor-client-serialization-jvm:${ktorVersion}")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${coroutinesVersion}")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime:${kotlinxSerializationVersion}")

    implementation("com.github.ajalt:clikt:$cliktVersion")
    implementation("com.sksamuel.avro4k:avro4k-core:$avro4kVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${coroutinesVersion}")
}

application {
    mainClassName = "com.codingventures.movies.moviecmd.MainKt"
}

val jar by tasks.getting(Jar::class) {
    manifest {
        attributes["Main-Class"] = "com.codingventures.movies.moviecmd.MainKt"
    }
    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) }) {
        exclude("META-INF/*.RSA", "META-INF/*.SF", "META-INF/*.DSA")
    }
}