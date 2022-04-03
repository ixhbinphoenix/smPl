package me.ixhbinphoenix.smPl.smItems.commands

import me.ixhbinphoenix.smPl.smCore.commands.BaseCommand
import me.ixhbinphoenix.smPl.smItems.item.ItemHandler
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class setItemXPCommand : BaseCommand {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender is Player) {
            if (args.isNotEmpty()) {
                val num = args[0].toIntOrNull()
                if (num is Int) {
                    val item = sender.inventory.itemInMainHand
                    if (item.hasItemMeta()) {
                        val handler = ItemHandler(sender.inventory.itemInMainHand, sender)
                        handler.setXP(num)
                        sender.sendMessage(
                            Component.text("Set ").color(NamedTextColor.GOLD)
                                .append(item.displayName())
                                .append(Component.text(" XP to ").color(NamedTextColor.GOLD))
                                .append(Component.text(num).color(NamedTextColor.GREEN))
                        )
                    }
                } else {
                    sender.sendMessage(Component.text("${args[0]} is not an Integer!").color(NamedTextColor.RED))
                }
            } else {
                sender.sendMessage(Component.text("Not enough arguments!").color(NamedTextColor.RED))
            }
        }
        return true
    }

    override fun onTabComplete(sender: CommandSender, command: Command, label: String, args: Array<out String>): MutableList<String>? {
        return null
    }
}