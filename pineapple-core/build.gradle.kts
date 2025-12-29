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
    testImplementation(libs.paper.api)
    testImplementation(projects.pineappleCommon)

    testImplementation("com.google.code.gson:gson:2.11.0")
    testImplementation(platform("org.junit:junit-bom:6.0.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
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