rootProject.name = "Pineapple"

gradle.rootProject {
    group = "sh.miles"
}

include(
    "paper-bundle",
    "paper-bundle:paper-core",
    "paper-common"
)
