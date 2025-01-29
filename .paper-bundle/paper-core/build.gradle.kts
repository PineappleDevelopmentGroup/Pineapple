plugins {
    kotlin("jvm") version "2.0.10"
}

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.3-R0.1-SNAPSHOT")
    compileOnly(project(":paper-common"))
}

kotlin {
    jvmToolchain(21)
}
