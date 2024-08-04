rootProject.name = "Pineapple"

gradle.rootProject {
    group = "sh.miles.testing"
}

include(
    "pineapple-common",
    "pineapple-bundle",
    "pineapple-infstack",
    "pineapple-bundle:pineapple-core",
    "pineapple-bundle:pineapple-nms",
    "pineapple-bundle:pineapple-nms:api",
    "pineapple-bundle:pineapple-nms:spigot-v1_20_R3",
    "pineapple-bundle:pineapple-nms:spigot-v1_20_R4",
    "pineapple-bundle:pineapple-nms:spigot-v1_21_R1"
)
