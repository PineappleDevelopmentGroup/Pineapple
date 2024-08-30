plugins {
    id("pineapple-module-simple")
    id("pineapple-module-publishing")
}

version = "1.0.0-SNAPSHOT"

repositories {
    maven("https://maven.miles.sh/pineapple")
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly(libs.spigot.api)

    compileOnly(project(":pineapple-bundle:pineapple-core"))
    compileOnly(project(":pineapple-common"))

    testImplementation("sh.miles:pineapple-bundle:1.0.0-SNAPSHOT")
    testImplementation("com.github.seeseemelk:MockBukkit-v1.20:3.78.0")
    testCompileOnly("io.papermc.paper:paper-api:1.20.4-R0.1-SNAPSHOT")
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

