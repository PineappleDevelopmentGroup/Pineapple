import com.github.jengelman.gradle.plugins.shadow.ShadowExtension

plugins {
    id("pineapple-module-simple")
    id("pineapple-module-publishing")
    alias(libs.plugins.shadow)
    alias(libs.plugins.javadoc)
}

version = "1.0.0-SNAPSHOT"

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
    api(project(":pineapple-common"))
    implementation(project(":pineapple-bundle:pineapple-core"))
    implementation(project(":pineapple-bundle:pineapple-nms"))
}

tasks.jar {
    enabled = false
}

tasks.shadowJar {
    archiveClassifier = ""
    archiveVersion = ""
}

val aggregateCheckstyle by tasks.register<DefaultTask>("aggregateCheckstyle") {
    for (subproject in subprojects) {
        if (subproject.pluginManager.hasPlugin("pineapple-checkstyle")) {
            dependsOn(subproject.tasks.getByPath("checkstyleMain"))
        }
    }
}

tasks.publish {
    dependsOn(aggregateCheckstyle)
}

tasks.publishToMavenLocal {
    dependsOn(aggregateCheckstyle)
}

val aggregateSources by tasks.register<Jar>("aggregateSources") {
    archiveClassifier.set("sources")
    val sourcesJars: MutableList<TaskProvider<Jar>> = mutableListOf()
    for (subproject in subprojects) {
        if (subproject.tasks.findByName("sourcesJar") == null) continue
        sourcesJars.add(subproject.tasks.named<Jar>("sourcesJar"))
    }

    dependsOn(sourcesJars)

    sourcesJars.forEach { sourcesJarTask ->
        from(sourcesJarTask.flatMap { it.archiveFile.map { zipTree(it) } })
    }
}

tasks.build {
    dependsOn(tasks.shadowJar)
    dependsOn(aggregateCheckstyle)
}

publishing {
    publications {
        create<MavenPublication>("Maven") {
            shadow.component(this)
            this.artifact(tasks.aggregateJavadocJar)
            this.artifact(aggregateSources)

            group = rootProject.group
            version = project.version as String
        }
    }
}
