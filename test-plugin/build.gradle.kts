plugins {
    kotlin("jvm")
    id("com.gradleup.shadow")
    id("xyz.jpenilla.run-paper") version "3.0.2"
}

dependencies {
    compileOnly(libs.paper.api)
    implementation(projects.pineappleCore)
    implementation(projects.pineappleCommon)
    implementation(projects.pineappleNms.api)
}

tasks.runServer {
    minecraftVersion(libs.versions.paper.api.version.get().split("-").first())

    workingDir = file("run")
    systemProperty("net.kyori.adventure.text.warnWhenLegacyFormattingDetected", true)

    doFirst {
        workingDir.mkdirs()
    }
}