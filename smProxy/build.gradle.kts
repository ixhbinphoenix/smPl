
plugins {
    kotlin("jvm") version "1.6.20"
    kotlin("kapt")
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
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}