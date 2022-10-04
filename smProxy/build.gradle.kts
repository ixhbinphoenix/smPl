
plugins {
    kotlin("jvm") version "1.6.20"
    kotlin("kapt")
    id("org.jetbrains.kotlin.plugin.serialization") version "1.6.20"
    java
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven {
        name = "velocity"
        url = uri("https://nexus.velocitypowered.com/repository/maven-public/")
    }
}

dependencies {
    implementation("com.velocitypowered:velocity-api:3.1.0")
    kapt("com.velocitypowered:velocity-api:3.1.0")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.0")
    implementation("org.jetbrains.exposed:exposed-core:0.39.2")
    implementation("org.jetbrains.exposed:exposed-dao:0.39.2")
    implementation("org.jetbrains.exposed:exposed-jdbc:0.39.2")
    implementation("org.jetbrains.exposed:exposed-java-time:0.39.2")
    implementation("org.postgresql:postgresql:42.5.0")

    implementation(kotlin("stdlib"))
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(11))
}

tasks {
    assemble {
        dependsOn(shadowJar)
    }
    build {
        dependsOn(shadowJar)
    }
    shadowJar {
        destinationDirectory.set(file("../output/velocity/"))
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions.freeCompilerArgs += "-opt-in=kotlin.RequiresOptIn"
}