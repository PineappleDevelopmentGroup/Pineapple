plugins {
//    id("pineapple-checkstyle")
    java
    `pineapple-publishing-java`
}

java {
    withSourcesJar()
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}


dependencies {
    compileOnly("org.jetbrains:annotations-java5:24.0.1")
    compileOnly(libs.paper.api)
    compileOnly(project(":pineapple-common"))
}

publishing {
    publications {
        create<MavenPublication>("NMS-Api") {
            from(components["java"])

            group = rootProject.group
            version = parent!!.version as String
        }
    }
}
