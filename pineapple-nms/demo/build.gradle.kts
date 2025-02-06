import io.papermc.paperweight.userdev.ReobfArtifactConfiguration

plugins {
    java
    id("io.papermc.paperweight.userdev") version "2.0.0-beta.14"
}

dependencies {
    paperweight.paperDevBundle("1.21.4-R0.1-SNAPSHOT")
}

paperweight {
    this.reobfArtifactConfiguration.set(ReobfArtifactConfiguration.MOJANG_PRODUCTION)
}