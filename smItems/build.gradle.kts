plugins {
    java
    idea
    kotlin("jvm") version "1.6.20"
    `java-library`
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("io.papermc.paperweight.userdev") version "1.3.5"
}

group = "me.ixhbinphoenix.smPl"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()

    maven(url = "https://oss.sonatype.org/content/repositories/snapshots")
    maven(url = uri("https://papermc.io/repo/repository/maven-public/"))
}

dependencies {
    paperDevBundle("1.18.2-R0.1-SNAPSHOT")

    compileOnly(project(":smCore"))
    compileOnly(project(":smChat"))
    compileOnly(project(":smEntities"))

    implementation("org.jetbrains.exposed:exposed-core:0.38.1")
    implementation("org.jetbrains.exposed:exposed-dao:0.38.1")
    implementation("org.jetbrains.exposed:exposed-jdbc:0.38.1")
    implementation("org.jetbrains.exposed:exposed-java-time:0.38.1")
    implementation("org.postgresql:postgresql:42.3.3")

    implementation(kotlin("stdlib"))
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

tasks {
    assemble {
        dependsOn(reobfJar)
    }

    compileJava {
        options.encoding = Charsets.UTF_8.name()

        options.release.set(17)
    }
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

