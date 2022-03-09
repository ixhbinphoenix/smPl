package me.ixhbinphoenix.smPl.smCore

import org.bukkit.plugin.java.JavaPlugin

class Main : JavaPlugin() {
  override fun onEnable() {
    server.consoleSender.sendMessage("smCore enabled")
  }
}