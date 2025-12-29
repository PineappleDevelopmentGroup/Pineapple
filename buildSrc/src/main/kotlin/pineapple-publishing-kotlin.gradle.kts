import gradle.kotlin.dsl.accessors._4030ec6ddab2b564fb77e4054b09ecb0.publishing

plugins {
    kotlin("jvm")
    `maven-publish`
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
}