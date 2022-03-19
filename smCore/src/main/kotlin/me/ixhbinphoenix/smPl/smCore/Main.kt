package me.ixhbinphoenix.smPl.smCore

import me.ixhbinphoenix.smPl.smCore.commands.Commands
import me.ixhbinphoenix.smPl.smCore.events.Events
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.plugin.java.JavaPlugin

@Suppress("unused")
class Main : JavaPlugin() {
  private val cmds = Commands()
  override fun onEnable() {
    for (cmd in cmds.cmds) {
      getCommand(cmd)?.setExecutor(cmds)
    }
    server.pluginManager.registerEvents(Events(), this)
    server.consoleSender.sendMessage(Component.text("smCore enabled").color(NamedTextColor.GREEN))
  }
}