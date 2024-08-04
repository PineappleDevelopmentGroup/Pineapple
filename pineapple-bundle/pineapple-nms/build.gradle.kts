plugins {
    java
}

version = parent!!.version

repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {
    for (subproject in subprojects) {
        implementation(subproject)
    }
}
