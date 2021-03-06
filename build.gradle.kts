/*
 * This file was generated by the Gradle 'init' task.
 *
 * This is a general purpose Gradle build.
 * Learn how to create Gradle builds at https://guides.gradle.org/creating-new-gradle-builds
 */
import dependencies.kotlinLogVersion
import dependencies.simpleLogVersion
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import dependencies.kotestVersion

plugins {
    kotlin("jvm") apply false
    kotlin("kapt") version "1.4.32" apply false
    id("org.jetbrains.kotlin.plugin.serialization") apply false
    id("com.google.cloud.tools.jib") version "2.4.0" apply false
    id("org.flywaydb.flyway") version "6.5.3" apply false
    id("com.palantir.git-version") version "0.12.3"
}

allprojects {
    group = "com.codingventures"
    apply(plugin = "com.palantir.git-version")

    val gitVersion: groovy.lang.Closure<*> by extra
    version = gitVersion()

    repositories {
        jcenter()
        mavenCentral()
        maven("http://packages.confluent.io/maven/")
        maven("https://dl.bintray.com/arrow-kt/arrow-kt/")
    }
}

configure(subprojects.filter { it.name != "modules" }) {
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "org.jetbrains.kotlin.plugin.serialization")

    tasks {
        withType<KotlinCompile> {
            kotlinOptions {
                freeCompilerArgs = listOf("-Xjsr305=strict")
                jvmTarget = "1.8"
                allWarningsAsErrors = false
            }
        }

        withType<JavaCompile> {
            sourceCompatibility = JavaVersion.VERSION_1_8.toString()
            targetCompatibility = JavaVersion.VERSION_1_8.toString()
        }

        withType<Test> {
            useJUnitPlatform { }
        }
    }

    dependencies {
        "implementation"(kotlin("stdlib-jdk8"))
        "implementation"(kotlin("script-runtime"))
        "implementation"("io.github.microutils:kotlin-logging:$kotlinLogVersion")
        "implementation"("org.slf4j:slf4j-simple:$simpleLogVersion")

        "testImplementation"("io.kotest:kotest-runner-junit5-jvm:$kotestVersion")
        "testImplementation"("io.kotest:kotest-assertions-core-jvm:$kotestVersion")
        "testImplementation"("io.kotest:kotest-property-jvm:$kotestVersion")
    }
}

