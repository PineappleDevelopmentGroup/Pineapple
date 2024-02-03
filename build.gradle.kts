plugins {
    id("checkstyle")
    id("java-library")
    id("maven-publish")
    id("io.freefair.aggregate-javadoc-jar") version "8.4"
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

dependencies {
    implementation(project(":pineapple-utils"))
    implementation(project(":pineapple-core"))
    implementation(project(":pineapple-nms"))
}

tasks.jar {
    // shadowJar handles this
    this.enabled = false
}

tasks.shadowJar {
    this.archiveClassifier.set("")
    this.archiveVersion.set("")

    this.relocate("com.jeff_media.morepersistentdatatypes", "sh.miles.pineapple.pdc.morepdc")
}

tasks.build {
    this.dependsOn(tasks.shadowJar)
}

subprojects {
    apply(plugin = "java")

    dependencies {
        testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
        testRuntimeOnly ("org.junit.jupiter:junit-jupiter-engine:5.8.1")

        compileOnly ("org.jetbrains:annotations-java5:24.0.1")
    }

    tasks.compileJava {
        options.encoding = "UTF-8"
    }

    tasks.test {
        useJUnitPlatform()
        testLogging {
            events("passed", "skipped", "failed")
        }
    }

    tasks.withType<Javadoc> {
        exclude("sh/miles/pineapple/nms/impl/**")
    }
}

tasks.withType<Javadoc> {
    val options = this.options
    if (options !is StandardJavadocDocletOptions) return@withType
    options.addStringOption("Xdoclint:none", "-quiet")
    options.links(
        "https://docs.oracle.com/en/java/javase/17/docs/api/",
        "https://hub.spigotmc.org/javadocs/spigot/",
        "https://javadoc.io/doc/org.jetbrains/annotations-java5/24.0.1",
        "https://repo.jeff-media.com/javadoc/public/com/jeff_media/MorePersistentDataTypes/2.4.0/raw/"
    )
}

publishing {
    publications {
        create<MavenPublication>("Maven") {
            project.shadow.component(this)
            this.artifact(tasks.aggregateJavadocJar)
        }
    }

    repositories {
        maven {
            this.url = uri("https://maven.miles.sh/libraries")
            credentials {
                this.username = System.getenv("PINEAPPLE_REPOSILITE_USERNAME")
                this.password = System.getenv("PINEAPPLE_REPOSILITE_PASSWORD")
            }
        }
    }
}
