plugins {
  java
  idea
  kotlin("jvm") version "1.6.10"
  id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "me.ixhbinphoenix.smPl"
version = "1.0-SNAPSHOT"

repositories {
  mavenCentral()

  maven(url = "https://oss.sonatype.org/content/repositories/snapshots")
  maven(url = "https://hub.spigotmc.org/nexus/content/repositories/snapshots")
}

dependencies {
  compileOnly("org.spigotmc:spigot-api:1.18.2-R0.1-SNAPSHOT")

  implementation(kotlin("stdlib"))
}

tasks.getByName<Test>("test") {
  useJUnitPlatform()
}

