plugins {
    id("java")
}

group = "pineapple-nms"

dependencies {
    for (subproject in subprojects) {
        implementation(subproject)
    }
}

