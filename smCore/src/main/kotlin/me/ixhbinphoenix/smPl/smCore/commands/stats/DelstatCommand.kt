package me.ixhbinphoenix.smPl.smCore.commands.stats

import me.ixhbinphoenix.smPl.smCore.commands.BaseCommand
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.NamespacedKey
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class DelstatCommand : BaseCommand {
  override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
    if (sender is Player) {
      if (args.size == 1) {
        sender.persistentDataContainer.remove(NamespacedKey.fromString(args[0])!!)
      } else {
        val msg = Component.text("Not enough or too many arguments!").color(NamedTextColor.RED)
        sender.sendMessage(msg)
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
    if (args.size == 1 && sender is Player) {
      val list = ArrayList<String>()
      for (key in sender.persistentDataContainer.keys) {
        list.add(key.key)
      }
      return list
    }
    return null
  }
}