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

    testCompileOnly(projects.pineappleCore)
    testCompileOnly(projects.pineappleCommon)
    testImplementation("com.github.seeseemelk:MockBukkit-v1.20:3.78.0")
    testCompileOnly(libs.paper.api)
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
}
