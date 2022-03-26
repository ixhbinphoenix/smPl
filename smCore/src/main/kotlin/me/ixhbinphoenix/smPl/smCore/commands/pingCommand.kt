package me.ixhbinphoenix.smPl.smCore.commands

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.command.Command
import org.bukkit.command.CommandSender

class pingCommand : BaseCommand {
  override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
    val msg: TextComponent = Component.text("Pong!").color(NamedTextColor.GREEN)
    sender.sendMessage(msg)
    return true
  }

  override fun onTabComplete(sender: CommandSender, command: Command, label: String, args: Array<out String>): MutableList<String>? {
    return null
  }
}