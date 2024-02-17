rootProject.name = "Pineapple"

gradle.rootProject {
    this.version = "1.0.0-SNAPSHOT"
    this.group = "sh.miles"
}

dependencyResolutionManagement {
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
}

include("pineapple-utils", "pineapple-nms", "pineapple-core")

file("pineapple-nms").listFiles()?.forEach { project ->
    if (project.resolve("build.gradle.kts").exists()) run {
        include("pineapple-nms:${project.name}")
    }
}
