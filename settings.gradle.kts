
pluginManagement {
  repositories {
    gradlePluginPortal()
    maven(url = uri("https://papermc.io/repo/repository/maven-public/"))
  }
}

rootProject.name = "smPl"
include("smChat")
include("smCore")
include("smProxy")
