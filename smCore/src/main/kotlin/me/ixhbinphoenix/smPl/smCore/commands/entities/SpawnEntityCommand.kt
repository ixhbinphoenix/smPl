package me.ixhbinphoenix.smPl.smCore.commands.entities

import me.ixhbinphoenix.smPl.smCore.commands.BaseCommand
import me.ixhbinphoenix.smPl.smCore.entities.EntityUtils
import me.ixhbinphoenix.smPl.smCore.entities.EntityHandler
import me.ixhbinphoenix.smPl.smCore.entities.BaseEntityHandler
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor

class SpawnEntityCommand : BaseCommand {
    private val entityUtils = EntityUtils()

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if(sender is Player) {
            if (args.isNotEmpty()) {
                val data = entityUtils.entities[args[0]]
                if (data is BaseEntityHandler) {
                    EntityHandler.spawn(data.data, sender.world, sender.location)
                } else {
                    sender.sendMessage(Component.text("${args[0]} is not a mob!").color(NamedTextColor.RED))
                }
            } else {
                sender.sendMessage(Component.text("Not enough arguments!").color(NamedTextColor.RED))
            }
        }
        return true
    }

    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>
    ): MutableList<String>? {
        if (args.size == 1) {
            return entityUtils.entities.keys.toMutableList()
        }
        return null
    }
}
