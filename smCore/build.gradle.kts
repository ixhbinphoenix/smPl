plugins {
  java
  idea
  kotlin("jvm") version "1.6.10"
  id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "io.github.ixhbinphoenix"
version = "1.0-SNAPSHOT"

repositories {
  mavenCentral()

  maven(url = "https://oss.sonatype.org/content/repositories/snapshots")
  maven(url = "https://hub.spigotmc.org/nexus/content/repositories/snapshots")
}

dependencies {
  compileOnly("org.spigotmc:spigot-api:1.18.2-R0.1-SNAPSHOT")

  implementation(kotlin("stdlib"))

  testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
  testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

tasks.getByName<Test>("test") {
  useJUnitPlatform()
}

