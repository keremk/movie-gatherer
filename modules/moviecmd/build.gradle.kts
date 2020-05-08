import dependencies.avro4kVersion
import dependencies.cliktVersion

plugins {
    application
}

dependencies {
    implementation(project(":modules:movies-domain"))
    implementation(project(":modules:ingester"))

    implementation("com.github.ajalt:clikt:$cliktVersion")
    implementation("com.sksamuel.avro4k:avro4k-core:$avro4kVersion")
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