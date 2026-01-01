plugins {
    alias(libs.plugins.shadow)
    java
    `maven-publish`
}

repositories {
    mavenCentral()
    maven("https://maven.miles.sh/pineapple")
}

dependencies {
    shadow(projects.pineappleCore)
    shadow(projects.pineappleCommon)
    shadow(project(":pineapple-nms:api"))

}

tasks.publish {
    dependsOn(subprojects.flatMap { it.getTasksByName("check", false) })
}

tasks.publishToMavenLocal {
    dependsOn(subprojects.flatMap { it.getTasksByName("check", false) })
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