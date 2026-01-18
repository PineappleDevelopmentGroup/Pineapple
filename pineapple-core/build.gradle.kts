plugins {
    java
    `pineapple-publishing-java`
    `pineapple-checkstyle`
}

version = "1.0.0-SNAPSHOT"

dependencies {
    compileOnly(libs.paper.api)
    compileOnly(projects.pineappleCommon)
    compileOnly(projects.pineappleNms.api)

    testImplementation(platform(libs.junit.bom))
    testImplementation(libs.junit.jupiter)
    testRuntimeOnly(libs.junit.launcher)

    testImplementation(libs.mockbukkit)
    testImplementation(libs.paper.api)
    testImplementation(projects.pineappleCommon)
}

tasks.test {
    useJUnitPlatform()
}

java {
    withJavadocJar()
    withSourcesJar()
}

publishing {
    publications {
        create<MavenPublication>("Core") {
            from(components["java"])

            group = rootProject.group
            version = project.version as String
        }
    }
}