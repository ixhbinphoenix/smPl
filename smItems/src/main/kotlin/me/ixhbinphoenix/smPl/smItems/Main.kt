package me.ixhbinphoenix.smPl.smItems

import me.ixhbinphoenix.smPl.smCore.commands.BaseCommand
import me.ixhbinphoenix.smPl.smItems.commands.giveItemCommand
import me.ixhbinphoenix.smPl.smItems.commands.metaCommand
import me.ixhbinphoenix.smPl.smItems.commands.setItemXPCommand
import me.ixhbinphoenix.smPl.smItems.events.Events
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.plugin.java.JavaPlugin

@Suppress("unused")
class Main : JavaPlugin() {
    init {
        instance = this
    }

    override fun onEnable() {
        val commands: HashMap<String, BaseCommand> = hashMapOf(
            "giveitem" to giveItemCommand(),
            "meta" to metaCommand(),
            "setitemxp" to setItemXPCommand()
        )
        for (cmd in commands) {
            getCommand(cmd.key)?.setExecutor(cmd.value)
            getCommand(cmd.key)?.tabCompleter = cmd.value
        }
        server.pluginManager.registerEvents(Events(), this)

        server.consoleSender.sendMessage(Component.text("smItems enabled").color(NamedTextColor.GREEN))
    }
    companion object {
        lateinit var instance: Main
    }
}

fun getInstance(): Main {
    return Main.instance
}