rootProject.name = "Pineapple"

gradle.rootProject {
    group = "sh.miles"
    version = "1.0.0-SNAPSHOT"
}

include(
    "paper-bundle",
    "paper-bundle:paper-core",
    "paper-common"
)
