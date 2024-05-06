plugins {
    id("pineappleremap")
}

dependencies {
    compileOnly("org.spigotmc:spigot:1.20.4-R0.1-SNAPSHOT:remapped-mojang")
    compileOnly(project(":pineapple-utils"))
}

tasks.remap {
    this.version.set("1.20.4")
}
