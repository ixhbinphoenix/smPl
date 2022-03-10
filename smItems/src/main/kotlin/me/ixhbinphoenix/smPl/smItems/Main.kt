package me.ixhbinphoenix.smPl.smItems

import org.bukkit.plugin.java.JavaPlugin

@Suppress("unused")
class Main : JavaPlugin() {
    override fun onEnable() {
        server.consoleSender.sendMessage("§asmItems enabled")
    }
}