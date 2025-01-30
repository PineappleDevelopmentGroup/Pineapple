plugins {
    id("com.diffplug.spotless")
}

spotless {
    kotlin {
        ktlint("1.5.0").setEditorConfigPath(rootProject.projectDir.resolve(".editorconfig"))
    }
    java {
    }
}