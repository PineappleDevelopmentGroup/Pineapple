plugins {
    id("pineapplecheckstyle")
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17

    withJavadocJar()
}

val compileOnlyAndTest = configurations.create("compileOnlyAndTest")
configurations {
    compileOnlyAndTest.setTransitive(false)
    testImplementation.get().extendsFrom(compileOnlyAndTest)
    compileOnly.get().extendsFrom(compileOnlyAndTest)
}

dependencies {
    testImplementation("com.github.seeseemelk:MockBukkit-v1.20:3.9.0")
    compileOnly("org.spigotmc:spigot-api:1.20.4-R0.1-SNAPSHOT")

    implementation("sh.miles.pineapplechat:pineapplechat-core:1.0.0-SNAPSHOT")
    implementation("sh.miles.pineapplechat:pineapplechat-bungee:1.0.0-SNAPSHOT")
    implementation("sh.miles.pineapplechat:pineapplechat-minecraft-legacy:1.0.0-SNAPSHOT")
    implementation("com.jeff_media:MorePersistentDataTypes:2.4.0")

    compileOnly(project(":pineapple-nms:api"))
    compileOnlyAndTest(project(":pineapple-utils"))
}

tasks.processResources {
    expand("version" to rootProject.version)
}
