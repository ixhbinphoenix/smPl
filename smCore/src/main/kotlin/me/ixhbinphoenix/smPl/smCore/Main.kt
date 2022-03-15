package me.ixhbinphoenix.smPl.smCore

import me.ixhbinphoenix.smPl.smCore.events.Events
import org.bukkit.plugin.java.JavaPlugin

class Main : JavaPlugin() {
  private val cmds = Commands()
  override fun onEnable() {
    for (cmd in cmds.cmds) {
      getCommand(cmd)?.setExecutor(cmds)
    }
    server.pluginManager.registerEvents(Events(), this)
    server.consoleSender.sendMessage("§asmCore enabled")
  }
}