plugins {
    id("pineapple-module-simple")
}

val compileOnlyAndTest = configurations.create("compileOnlyAndTest")
configurations {
    compileOnlyAndTest.setTransitive(false)
    testImplementation.get().extendsFrom(compileOnlyAndTest)
    compileOnly.get().extendsFrom(compileOnlyAndTest)
}

repositories {
    mavenCentral()
    mavenLocal()
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://oss.sonatype.org/content/repositories/central")
    maven("https://libraries.minecraft.net/")
    maven("https://repo.jeff-media.com/public")
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://maven.miles.sh/libraries")
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")

    testImplementation(libs.mockbukkit)
    compileOnly(libs.spigot.api)

    // Deprecated
    implementation(libs.pineapplechat.core)
    implementation(libs.pineapplechat.bungee)
    implementation(libs.pineapplechat.legacy)
    // Deprecated End
    implementation(libs.adventure)
    implementation(libs.minimessage)

    compileOnly(project(":pineapple-bundle:pineapple-nms:api"))
    compileOnlyAndTest(project(":pineapple-common"))
}

tasks.processResources {
    expand("version" to rootProject.version)
}
