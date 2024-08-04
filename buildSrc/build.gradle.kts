plugins {
    `kotlin-dsl`
    alias(libs.plugins.jvm)
}

repositories {
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    implementation(libs.patrick.remapper)
}
