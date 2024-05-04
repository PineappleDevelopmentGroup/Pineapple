plugins {
    `kotlin-dsl`
    kotlin("jvm") version "1.9.20"
}

repositories {
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    implementation("io.github.patrick.remapper:io.github.patrick.remapper.gradle.plugin:1.4.1")
}
