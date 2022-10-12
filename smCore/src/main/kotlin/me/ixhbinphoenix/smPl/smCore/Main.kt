package me.ixhbinphoenix.smPl.smCore

import me.ixhbinphoenix.smPl.smCore.commands.*
import me.ixhbinphoenix.smPl.smCore.commands.entities.SpawnEntityCommand
import me.ixhbinphoenix.smPl.smCore.commands.items.giveItemCommand
import me.ixhbinphoenix.smPl.smCore.commands.items.giveResourceCommand
import me.ixhbinphoenix.smPl.smCore.commands.items.metaCommand
import me.ixhbinphoenix.smPl.smCore.commands.items.setItemXPCommand
import me.ixhbinphoenix.smPl.smCore.commands.stats.delstatCommand
import me.ixhbinphoenix.smPl.smCore.commands.stats.pstatsCommand
import me.ixhbinphoenix.smPl.smCore.commands.stats.setstatCommand
import me.ixhbinphoenix.smPl.smCore.events.Events
import me.ixhbinphoenix.smPl.smCore.db.EquipmentUtils
import me.ixhbinphoenix.smPl.smCore.db.ResourceUtils
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.plugin.java.JavaPlugin
import org.jetbrains.exposed.sql.Database

@Suppress("unused")
class Main : JavaPlugin() {
  val dbConnection: Database

  init {
    instance = this
    config.options().copyDefaults(true)
    saveDefaultConfig()
    dbConnection = Database.connect(
      config.getString("db.jdbcConnectionString")!!,
      driver = "org.postgresql.Driver",
      user = config.getString("db.user")!!,
      password = config.getString("db.password")!!
    )
    EquipmentUtils.setupDB()
    ResourceUtils.setupDB()
  }

  override fun onEnable() {
    val commands = hashMapOf(
      "pstats" to pstatsCommand(),
      "setstat" to setstatCommand(),
      "delstat" to delstatCommand(),
      "spawnentity" to SpawnEntityCommand(),
      "giveitem" to giveItemCommand(),
      "giveresource" to giveResourceCommand(),
      "meta" to metaCommand(),
      "setitemxp" to setItemXPCommand()
    )
    for (cmd in commands) {
      getCommand(cmd.key)?.setExecutor(cmd.value)
      getCommand(cmd.key)?.tabCompleter = cmd.value
    }
    server.pluginManager.registerEvents(Events(), this)
    server.consoleSender.sendMessage(Component.text("smCore enabled").color(NamedTextColor.GREEN))
  }

  companion object {
    lateinit var instance: Main
  }
}

fun getInstance(): Main {
  return Main.instance
}