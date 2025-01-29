plugins {
    alias(libs.plugins.shadow)
    kotlin("jvm")
    `maven-publish`
}

repositories {
    mavenCentral()
    maven("https://maven.miles.sh/libraries")
    maven("https://maven.miles.sh/pineapple")
}

dependencies {
    shadow(project(":pineapple-core"))
    shadow(project(":pineapple-common"))
    shadow(project(":pineapple-nms:api"))
    
    // Deprecated
    shadow(libs.pineapplechat.core)
    shadow(libs.pineapplechat.bungee)
    shadow(libs.pineapplechat.legacy)
    // Deprecated End
}

tasks.shadowJar {
    archiveClassifier = ""
    archiveVersion = ""
}

publishing {
    repositories {
        maven("https://maven.miles.sh/pineapple") {
            credentials {
                this.username = System.getenv("PINEAPPLE_REPOSILITE_USERNAME")
                this.password = System.getenv("PINEAPPLE_REPOSILITE_PASSWORD")
            }
        }
    }

    publications {
        create<MavenPublication>("Pineapple") {
            from(components["shadow"]) // or components["shadow"] in Kotlin DSL


            group = rootProject.group
            version = project.version as String
        }
    }
}