plugins {
    id("pineapplecheckstyle")
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17

    withJavadocJar()
    withSourcesJar()
}

val compileOnlyAndTest = configurations.create("compileOnlyAndTest")
configurations {
    compileOnlyAndTest.setTransitive(false)
    testImplementation.get().extendsFrom(compileOnlyAndTest)
    compileOnly.get().extendsFrom(compileOnlyAndTest)
}

dependencies {
    testImplementation(libs.mockbukkit)
    compileOnly(libs.spigot.api)

    // Deprecated
    implementation(libs.pineapplechat.core)
    implementation(libs.pineapplechat.bungee)
    implementation(libs.pineapplechat.legacy)
    // Deprecated End
    implementation(libs.morepdc)

    compileOnly(project(":pineapple-nms:api"))
    compileOnlyAndTest(project(":pineapple-utils"))
}

tasks.processResources {
    expand("version" to rootProject.version)
}
