package me.ixhbinphoenix.smPl.smProxy

import com.google.inject.Inject
import com.velocitypowered.api.event.PostOrder
import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent
import com.velocitypowered.api.plugin.Plugin
import com.velocitypowered.api.plugin.annotation.DataDirectory
import com.velocitypowered.api.proxy.ProxyServer
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import me.ixhbinphoenix.smPl.smProxy.chat.ChannelManager
import me.ixhbinphoenix.smPl.smProxy.commands.*
import me.ixhbinphoenix.smPl.smProxy.events.Events
import me.ixhbinphoenix.smPl.smProxy.uuid.UUIDCache
import org.jetbrains.exposed.sql.Database
import org.slf4j.Logger
import java.io.File
import java.io.IOException
import java.nio.file.Path

@Serializable
data class PluginConfig(val jdbcConnectionString: String, val postgresUser: String, val postgresPassword: String)

@Suppress("unused", "ClassName")
@Plugin(
  id = "smproxy",
  name = "smProxy",
  version = "0.0.2",
  url = "https://github.com/ixhbinphoenix/smPl",
  description = "Stahlmetall plugins",
  authors = ["ixhbinphoenix", "renkertm"]
)
class smProxy @Inject constructor(val server: ProxyServer, val logger: Logger, @DataDirectory val folder: Path) {

  val pluginConfig = loadConfig()
  val dbConnection: Database = loadDatabase()
  val uuidCache = UUIDCache()
  lateinit var channelManager: ChannelManager

  init {
    instance = this
  }

  @Subscribe(order = PostOrder.FIRST)
  fun onInit(event: ProxyInitializeEvent) {
    channelManager = ChannelManager()
    server.eventManager.register(this, Events())

    server.commandManager.register("sc", scCommand())
    server.commandManager.register("channel", channelCommand(), "chat")
    server.commandManager.register("broadcast", broadcastCommand(), "bc")
    server.commandManager.register("ping", pingCommand())
    server.commandManager.register("ban", banCommand())
    server.commandManager.register("unban", unbanCommand())

    logger.info("smProxy enabled")
  }

  @OptIn(ExperimentalSerializationApi::class)
  private fun loadConfig(): PluginConfig {
    val configFile = File(folder.toFile(), "config.json")
    if (!configFile.parentFile.exists()) {
      configFile.parentFile.mkdirs()
    }

    return if (!configFile.exists()) {
      try {
        val config = PluginConfig("", "root", "root")
        val emptyConfig = Json.encodeToString(config)
        configFile.createNewFile()
        configFile.writeText(emptyConfig)
        config
      } catch (e: IOException) {
        e.printStackTrace()
        PluginConfig("", "root", "root")
      }
    } else {
      Json.decodeFromStream(configFile.inputStream())
    }
  }

  private fun loadDatabase(): Database {
    return if (pluginConfig.jdbcConnectionString == "") {
      Database.connect(
        "jdbc:postgresql://localhost:5432/minecraft",
        driver = "org.postgresql.Driver",
        user = pluginConfig.postgresUser,
        password = pluginConfig.postgresPassword
      )
    } else {
      Database.connect(
        pluginConfig.jdbcConnectionString,
        driver = "org.postgresql.Driver",
        user = pluginConfig.postgresUser,
        password = pluginConfig.postgresPassword
      )
    }
  }

  companion object {
    lateinit var instance: smProxy
  }
}

fun getInstance(): smProxy {
  return smProxy.instance
}