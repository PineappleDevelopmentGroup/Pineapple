plugins {
    kotlin("jvm")
    id("com.gradleup.shadow")
    id("xyz.jpenilla.run-paper") version "2.3.1"
}

dependencies {
    compileOnly(libs.paper.api)
    implementation(project(":pineapple-core"))
    implementation(project(":pineapple-common"))
    implementation(project(":pineapple-nms:api"))
}

tasks.runServer {
    minecraftVersion("1.21.4")

    workingDir = file("run")
    systemProperty("net.kyori.adventure.text.warnWhenLegacyFormattingDetected", true)

    doFirst {
        workingDir.mkdirs()
    }
}