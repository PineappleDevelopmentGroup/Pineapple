plugins {
    id("java")
    id("checkstyle")
}

tasks.checkstyleMain {
    source = project.sourceSets.main.get().allJava.asFileTree
}

checkstyle {
    toolVersion = "10.12.5"
    configFile = file(rootDir.resolve("config/checkstyle/checkstyle.xml"))
    sourceSets = mutableListOf(project.sourceSets.main.get())
}

