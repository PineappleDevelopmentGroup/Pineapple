plugins {
    id("pineapple-api")
}

version = "1.0.0-SNAPSHOT"

repositories {
    maven("https://maven.miles.sh/pineapple")
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly(libs.paper.api)
    compileOnly(projects.pineappleCore)
    compileOnly(projects.pineappleCommon)
}