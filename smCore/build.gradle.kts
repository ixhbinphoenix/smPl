plugins {
  java
  idea
  kotlin("jvm") version "1.6.20"
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
  compileOnly(project(":smChat"))

  implementation("org.jetbrains.exposed:exposed-core:0.39.2")
  implementation("org.jetbrains.exposed:exposed-dao:0.39.2")
  implementation("org.jetbrains.exposed:exposed-jdbc:0.39.2")
  implementation("org.jetbrains.exposed:exposed-java-time:0.39.2")
  implementation("org.postgresql:postgresql:42.5.0")

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

  reobfJar {
    outputJar.set(file("../output/paper/smCore.jar"))
  }
}