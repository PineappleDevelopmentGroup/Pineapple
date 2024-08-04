plugins {
    `java-library`
    id("pineapple-checkstyle")
}

repositories {
    mavenCentral()
}

dependencies {
    compileOnly("org.jetbrains:annotations-java5:24.0.1")
}

java {
    disableAutoTargetJvm()
    toolchain.languageVersion = JavaLanguageVersion.of(21)
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks.compileJava {
    options.encoding = "UTF-8"
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        events("PASSED", "SKIPPED", "FAILED", "STANDARD_OUT", "STANDARD_ERROR")
    }
}

tasks.withType<Javadoc> {
    val options = this.options
    if (options !is StandardJavadocDocletOptions) return@withType
    options.addStringOption("Xdoclint:none", "-quiet")
    options.links(
        "https://docs.oracle.com/en/java/javase/17/docs/api/",
        "https://javadoc.io/doc/org.jetbrains/annotations-java5/24.0.1"
    )
    options.encoding = "UTF-8"
}


