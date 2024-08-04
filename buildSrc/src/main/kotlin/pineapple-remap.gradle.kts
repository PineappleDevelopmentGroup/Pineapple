plugins {
    java
    id("io.github.patrick.remapper")
}

dependencies {
    compileOnly(project(":pineapple-bundle:pineapple-nms:api"))
}

tasks.jar {
    finalizedBy(tasks.remap)
}
