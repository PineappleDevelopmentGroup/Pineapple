plugins {
    `pineapple-api`
}

group = "sh.miles.pineapple"

dependencies {
    compileOnly(libs.paper.api)

    compileOnly(project(":pineapple-core"))
    compileOnly(project(":pineapple-common"))
}