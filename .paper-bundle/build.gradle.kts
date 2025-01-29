plugins {
    kotlin("jvm") version "2.0.10"
    `maven-publish`
}

version = "${project.version}-jotlin"

repositories {
    mavenCentral()
}

dependencies {
    api(project(":paper-common"))
}

tasks.jar {
    enabled = false
}
