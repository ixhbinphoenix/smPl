
pluginManagement {
  repositories {
    gradlePluginPortal()
    maven(url = uri("https://papermc.io/repo/repository/maven-public/"))
  }
}

rootProject.name = "smPl"
include("smCore")
include("smItems")
include("smEntities")
include("smProxy")
