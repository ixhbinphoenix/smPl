package me.ixhbinphoenix.smPl.smEntities.commands

import me.ixhbinphoenix.smPl.smCore.commands.BaseCommand
import me.ixhbinphoenix.smPl.smEntities.entities.hostiles.Zombie
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class Commands : BaseCommand {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if(label == "zombie"){
            if(sender is Player){
                Zombie().spawn(sender.world, sender.location)
                return true
            }
        }
        return false
    }

    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>
    ): MutableList<String>? {
        when(label){
            "zombie" -> {
                return null
            }
        }
        return null
    }
}