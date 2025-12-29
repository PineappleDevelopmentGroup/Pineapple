rootProject.name = "Pineapple"

gradle.rootProject {
    group = "sh.miles"
    version = "1.0.0-SNAPSHOT"
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
        maven("https://repo.papermc.io/repository/maven-public/")
        maven("https://maven.miles.sh/pineapple")

    }
}

include(
    "pineapple-core",
    "pineapple-common",
    "pineapple-kotlin",
    "pineapple-nms",
    "pineapple-nms:api",
    "pineapple-dependency-loader"
)

file("pineapple-apis").listFiles()?.forEach { project ->
    if (project.resolve("build.gradle.kts").exists()) run {
        include("pineapple-apis:${project.name}")
    }
}

// Features
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")