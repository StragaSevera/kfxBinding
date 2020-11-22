group = "ru.ought.kfx_binding"
version = "0.1.0"

plugins {
    id("org.jetbrains.kotlin.jvm") version "1.4.10"
    `java-library`
    id("org.openjfx.javafxplugin") version "0.0.9"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.4.10")
}

kotlin {
    explicitApi()
}

javafx {
    modules("javafx.controls", "javafx.fxml")
}

tasks.jar {
    manifest {
        attributes(
            mapOf(
                "Implementation-Title" to project.name,
                "Implementation-Version" to project.version
            )
        )
    }
}