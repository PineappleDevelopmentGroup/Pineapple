//import org.jetbrains.dokka.base.DokkaBase
//import org.jetbrains.dokka.base.DokkaBaseConfiguration

plugins {
    java
//    id("org.jetbrains.dokka") version "1.9.20"
    `pineapple-publishing-java`
}

version = "1.0.0"
//
//buildscript {
//    dependencies {
//        classpath("org.jetbrains.dokka:dokka-base:1.9.20")
//    }
//}

repositories {
    mavenCentral()
}

dependencies {
//    dokkaHtmlPlugin("org.jetbrains.dokka:kotlin-as-java-plugin:1.9.20")
    compileOnly("org.jetbrains:annotations:26.0.0")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
}

java {
    withJavadocJar()
    withSourcesJar()
}

//kotlin {
//    compilerOptions {
//        jvmToolchain(21)
//    }
//}

//tasks.dokkaHtml {
//    pluginConfiguration<DokkaBase, DokkaBaseConfiguration> {
//        footerMessage = "(c) 2024 PineappleDevelopmentGroup"
//        separateInheritedMembers = true
//    }
//}
//
//val javadocJar by tasks.register<Jar>("dokkaJar") {
//    group = "documentation"
//    dependsOn(tasks.dokkaHtml)
//
//    from(tasks.dokkaHtml.flatMap { it.outputDirectory })
//    archiveClassifier.set("javadoc")
//}

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
        create<MavenPublication>("Common") {
            from(components["java"])
//            artifact(tasks.named("sourcesJar"))
//            artifact(javadocJar)

            group = rootProject.group
            version = project.version as String
        }
    }
}
