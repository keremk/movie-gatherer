import dependencies.autoServiceVersion
import dependencies.kotlinPoetVersion

plugins {
    kotlin("kapt")
}

dependencies {
    implementation(project(":modules:db-annotation"))

    implementation("com.squareup:kotlinpoet:$kotlinPoetVersion")
    implementation("com.google.auto.service:auto-service:$autoServiceVersion")

    kapt("com.google.auto.service:auto-service:$autoServiceVersion")
}