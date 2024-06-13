plugins {
    id("pineappleremap")
}

dependencies {
    compileOnly("org.spigotmc:spigot:1.21-R0.1-SNAPSHOT:remapped-mojang")
    compileOnly(project(":pineapple-utils"))
}

tasks.remap {
    this.version.set("1.21")
}