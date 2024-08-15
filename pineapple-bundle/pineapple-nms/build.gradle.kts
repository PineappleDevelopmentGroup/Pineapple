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

tasks.build {
    for (subproject in subprojects) {
        dependsOn(subproject.tasks.getByName("jar"))
    }
}
