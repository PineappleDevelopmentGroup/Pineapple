plugins {
    `kotlin-dsl`
    kotlin("jvm") version "1.9.20"
}

repositories {
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    implementation(libs.patrick.remapper)
}
