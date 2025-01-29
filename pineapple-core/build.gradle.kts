plugins {
    java
    `pineapple-publishing-java`
}

version = "1.0.0"

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.4-R0.1-SNAPSHOT")
    compileOnly(project(":pineapple-common"))
    compileOnly(project(":pineapple-nms:api"))

    testImplementation(libs.mockbukkit)
    testImplementation(project(":pineapple-common"))
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")

    // Deprecated
    compileOnly(libs.pineapplechat.core)
    compileOnly(libs.pineapplechat.bungee)
    compileOnly(libs.pineapplechat.legacy)
    // Deprecated End
}

java {
    withJavadocJar()
    withSourcesJar()
}

publishing {
    publications {
        create<MavenPublication>("PineappleCore") {
            from(components["java"])

            group = rootProject.group
            version = project.version as String
        }
    }
}