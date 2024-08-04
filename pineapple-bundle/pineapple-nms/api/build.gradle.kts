plugins {
    id("pineapple-checkstyle")
}

repositories {
    mavenCentral()
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
}

dependencies {
    compileOnly("org.jetbrains:annotations-java5:24.0.1")
    compileOnly(libs.spigot.api)
    compileOnly(project(":pineapple-common"))
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}
