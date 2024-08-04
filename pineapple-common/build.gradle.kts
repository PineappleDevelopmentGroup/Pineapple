plugins {
    id("pineapple-module-simple")
    id("pineapple-module-publishing")
}

version = "1.0.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
}

java {
    withSourcesJar()
    withJavadocJar()
}

publishing {
    publications {
        create<MavenPublication>("Maven") {
            from(components["java"])

            group = rootProject.group
            version = project.version as String
        }
    }
}
