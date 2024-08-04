plugins {
    id("pineapple-remap")
}

repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {
    compileOnly("org.jetbrains:annotations-java5:24.0.1")
    compileOnly("org.spigotmc:spigot:1.21-R0.1-SNAPSHOT:remapped-mojang")
    compileOnly(project(":pineapple-common"))
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

tasks.remap {
    this.version.set("1.21")
}
