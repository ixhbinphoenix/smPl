package me.ixhbinphoenix.smPl.smItems

import org.bukkit.plugin.java.JavaPlugin

class Main : JavaPlugin() {
    override fun onEnable() {
        server.consoleSender.sendMessage("§asmItems enabled")
    }
}