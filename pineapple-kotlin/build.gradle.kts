plugins {
    `pineapple-api`
}

group = "sh.miles"
version = "1.0.0"

dependencies {
    compileOnly(libs.paper.api)

    compileOnly(project(":pineapple-core"))
    compileOnly(project(":pineapple-common"))
}