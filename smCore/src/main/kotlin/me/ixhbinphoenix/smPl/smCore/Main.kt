package me.ixhbinphoenix.smPl.smCore

import org.bukkit.plugin.java.JavaPlugin

class Main : JavaPlugin() {
  private val cmds = Commands()
  override fun onEnable() {
    for (cmd in cmds.cmds) {
      getCommand(cmd)?.setExecutor(cmds)
    }

    server.consoleSender.sendMessage("§asmCore enabled")
  }
}