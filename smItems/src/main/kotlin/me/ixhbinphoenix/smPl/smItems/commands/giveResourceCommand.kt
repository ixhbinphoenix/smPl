package me.ixhbinphoenix.smPl.smItems.commands

import me.ixhbinphoenix.smPl.smCore.commands.BaseCommand
import me.ixhbinphoenix.smPl.smItems.db.ResourceUtils
import me.ixhbinphoenix.smPl.smItems.item.ItemUtils
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class giveResourceCommand : BaseCommand {
  private val itemUtils = ItemUtils()
  private val resourceUtils = ResourceUtils()

  override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
    if (sender is Player) {
      if (args.isNotEmpty()) {
        if (resourceUtils.getItem(args[0]) is ItemStack) {
          val item = resourceUtils.getItem(args[0])!!
          if (args.size > 1) {
            if (args[1].toIntOrNull() != null) {
              item.amount = args[1].toInt()
            } else {
              sender.sendMessage(Component.text("${args[1]} is not a number!").color(NamedTextColor.RED))
            }
          }
          itemUtils.giveItem(item, sender)
        } else {
          sender.sendMessage(Component.text("Unknown Item!").color(NamedTextColor.RED))
        }
      } else {
        sender.sendMessage(Component.text("Not enough arguments!").color(NamedTextColor.RED))
      }
    }
    return true
  }

  override fun onTabComplete(sender: CommandSender, command: Command, label: String, args: Array<out String>): MutableList<String>? {
    if (args.size == 1) {
      return resourceUtils.getRecomms(args[0])
    }
    return null
  }
}