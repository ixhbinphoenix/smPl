package me.ixhbinphoenix.smPl.smItems

import me.ixhbinphoenix.smPl.smItems.commands.Commands
import me.ixhbinphoenix.smPl.smItems.events.Events
import net.md_5.bungee.api.ChatColor
import org.bukkit.plugin.java.JavaPlugin

@Suppress("unused", "deprecation")
class Main : JavaPlugin() {
    private val cmds = Commands()
    override fun onEnable() {
        for (cmd in cmds.cmds) {
            getCommand(cmd)?.setExecutor(cmds)
        }
        server.pluginManager.registerEvents(Events(), this)

        server.consoleSender.sendMessage(ChatColor.GREEN.toString() + "smItems enabled")
    }
}