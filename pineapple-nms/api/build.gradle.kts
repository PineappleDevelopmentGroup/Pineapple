plugins {
//    id("pineapple-checkstyle")
    java
}

java {
    withSourcesJar()
}

dependencies {
    compileOnly("org.jetbrains:annotations-java5:24.0.1")
    compileOnly(libs.paper.api)
    compileOnly(project(":pineapple-common"))
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}
