plugins {
    id("pineappleremap")
}

dependencies {
    compileOnly("org.spigotmc:spigot:1.20.6-R0.1-SNAPSHOT:remapped-mojang")
    compileOnly(project(":pineapple-utils"))
}

tasks.remap {
    this.version.set("1.20.6")
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}
