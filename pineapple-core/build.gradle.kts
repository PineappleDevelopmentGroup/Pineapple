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

    testImplementation(libs.mockbukkit)
    testImplementation(projects.pineappleCommon)
    testImplementation("com.google.code.gson:gson:2.11.0")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")

}

java {
    withJavadocJar()
    withSourcesJar()
}

tasks.test {
    useJUnitPlatform()
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