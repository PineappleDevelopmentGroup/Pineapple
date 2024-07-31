plugins {
    id("pineapplecheckstyle")
}

dependencies {
    compileOnly(libs.spigot.api)
    compileOnly(project(":pineapple-utils"))
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}
