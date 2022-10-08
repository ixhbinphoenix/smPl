package me.ixhbinphoenix.smPl.smCore

import me.ixhbinphoenix.smPl.smCore.commands.*
import me.ixhbinphoenix.smPl.smCore.commands.entities.SpawnEntityCommand
import me.ixhbinphoenix.smPl.smCore.commands.stats.delstatCommand
import me.ixhbinphoenix.smPl.smCore.commands.stats.pstatsCommand
import me.ixhbinphoenix.smPl.smCore.commands.stats.setstatCommand
import me.ixhbinphoenix.smPl.smCore.events.Events
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.plugin.java.JavaPlugin

@Suppress("unused")
class Main : JavaPlugin() {
  init {
    instance = this
  }

  override fun onEnable() {
    val commands = HashMap<String, BaseCommand>()
    commands["pstats"] = pstatsCommand()
    commands["setstat"] = setstatCommand()
    commands["delstat"] = delstatCommand()
    commands["spawnentity"] = SpawnEntityCommand()
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