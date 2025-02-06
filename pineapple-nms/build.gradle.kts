plugins {
    java
    `pineapple-publishing-java`
    alias(libs.plugins.shadow)
}

version = "1.0.0-SNAPSHOT"

repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {
    for (subproject in subprojects) {
        if (subproject.name != "api") implementation(subproject)
    }
}

tasks.build {
    for (subproject in subprojects) {
        dependsOn(subproject.tasks.getByName("jar"))
    }
}

tasks.shadowJar {
    archiveClassifier = ""
    archiveVersion = ""
}

publishing {
    publications {
        create<MavenPublication>("NMS") {
            from(components["shadow"])

            group = rootProject.group
            version = project.version as String
        }
    }
}
