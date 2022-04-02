package me.ixhbinphoenix.smPl.smEntities

import me.ixhbinphoenix.smPl.smCore.commands.BaseCommand
import me.ixhbinphoenix.smPl.smEntities.entities.Npc
import me.ixhbinphoenix.smPl.smEntities.entities.hostiles.Zombie
import me.ixhbinphoenix.smPl.smEntities.events.Events
import org.bukkit.ChatColor
import org.bukkit.plugin.java.JavaPlugin

class Main : JavaPlugin() {
    init{
        instance = this
    }
    var npcList: MutableList<Npc> = emptyList<Npc>().toMutableList()
    override fun onEnable() {
        val commands: HashMap<String, BaseCommand> = hashMapOf(
            "zombie" to Zombie()
        )
        for(cmd in commands){
            getCommand(cmd.key)?.setExecutor(cmd.value)
            getCommand(cmd.key)?.tabCompleter = cmd.value
        }
        server.pluginManager.registerEvents(Events(), this)

        server.consoleSender.sendMessage(ChatColor.GREEN.toString() + "smEntities enabled")
    }
    companion object {
        lateinit var instance: Main
    }
}

@Suppress("unused")
fun getInstance(): Main{
    return Main.instance
}