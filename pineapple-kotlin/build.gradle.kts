plugins {
    `pineapple-api`
    `pineapple-checkstyle`
}

group = "sh.miles"
version = "1.0.0-SNAPSHOT"

dependencies {
    compileOnly(libs.paper.api)

    compileOnly(projects.pineappleCore)
    compileOnly(projects.pineappleCommon)
}