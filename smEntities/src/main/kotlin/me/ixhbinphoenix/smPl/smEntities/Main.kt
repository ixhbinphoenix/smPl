package me.ixhbinphoenix.smPl.smEntities

import me.ixhbinphoenix.smPl.smCore.commands.BaseCommand
import me.ixhbinphoenix.smPl.smEntities.commands.ZombieCommand
import me.ixhbinphoenix.smPl.smEntities.events.Events
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.plugin.java.JavaPlugin

class Main : JavaPlugin() {
    init{
        instance = this
    }
    override fun onEnable() {
        val commands: HashMap<String, BaseCommand> = hashMapOf(
            "zombie" to ZombieCommand()
        )
        for(cmd in commands){
            getCommand(cmd.key)?.setExecutor(cmd.value)
            getCommand(cmd.key)?.tabCompleter = cmd.value
        }
        server.pluginManager.registerEvents(Events(), this)

        server.consoleSender.sendMessage(Component.text("smEntities enabled!").color(NamedTextColor.GREEN))
    }
    companion object {
        lateinit var instance: Main
    }
}

@Suppress("unused")
fun getInstance(): Main{
    return Main.instance
}