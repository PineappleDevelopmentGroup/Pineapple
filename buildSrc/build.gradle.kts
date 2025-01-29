plugins {
    `kotlin-dsl`
    alias(libs.plugins.jvm)
}

repositories {
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    implementation(libs.kotlin)
    implementation(libs.dokka)
    implementation(libs.dokka.base)
}
