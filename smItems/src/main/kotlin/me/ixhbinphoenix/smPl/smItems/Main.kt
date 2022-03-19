package me.ixhbinphoenix.smPl.smItems

import me.ixhbinphoenix.smPl.smItems.commands.Commands
import me.ixhbinphoenix.smPl.smItems.events.Events
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

        server.consoleSender.sendMessage(Component.text("smItems enabled").color(NamedTextColor.GREEN))
    }
}