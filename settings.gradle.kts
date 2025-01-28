rootProject.name = "Pineapple"

gradle.rootProject {
    group = "sh.miles"
}

include(
    "pineapple-common",
    "pineapple-bundle",
    "pineapple-infstack",
    "pineapple-tiles",
    "pineapple-bundle:pineapple-core",
    "pineapple-bundle:pineapple-nms",
    "pineapple-bundle:pineapple-nms:api",
    "pineapple-bundle:pineapple-nms:spigot-v1_20_R3",
    "pineapple-bundle:pineapple-nms:spigot-v1_20_R4",
    "pineapple-bundle:pineapple-nms:spigot-v1_21_R1",
    "pineapple-bundle:pineapple-nms:spigot-v1_21_R2"
)
