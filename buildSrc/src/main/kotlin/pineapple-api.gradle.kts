plugins {
    kotlin("jvm")
    `maven-publish`
    id("org.jetbrains.dokka")
}

dependencies {
    dokkaHtmlPlugin("org.jetbrains.dokka:kotlin-as-java-plugin:2.0.0")
}

group = "sh.miles.pineapple.api"

java {
    withJavadocJar()
    withSourcesJar()
}

kotlin {
    compilerOptions {
        jvmToolchain(21)
    }
}

dokka {
    pluginsConfiguration.html {
        footerMessage = "(c) 2025 PineappleDevelopmentGroup"
        separateInheritedMembers = true
    }
}

val javadocJar by tasks.register<Jar>("dokkaJar") {
    group = "documentation"
    dependsOn(tasks.dokkaGenerateModuleHtml)

    from(tasks.dokkaGenerateModuleHtml.flatMap { it.outputDirectory })
    archiveClassifier.set("javadoc")
}

publishing {
    repositories {
        maven("https://maven.miles.sh/pineapple") {
            credentials {
                this.username = System.getenv("PINEAPPLE_REPOSILITE_USERNAME")
                this.password = System.getenv("PINEAPPLE_REPOSILITE_PASSWORD")
            }
        }
    }

    publications {
        create<MavenPublication>(project.name) {
            from(components["kotlin"])
            artifact(tasks.named("sourcesJar"))
            artifact(javadocJar)

            group = rootProject.group
            version = project.version as String
        }
    }
}
