package me.ixhbinphoenix.smPl.smCore.commands.stats

import me.ixhbinphoenix.smPl.smCore.commands.BaseCommand
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.NamespacedKey
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.persistence.PersistentDataType

class setstatCommand : BaseCommand {
  override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
    if (sender is Player) {
      if (args.size == 3) {
        when {
          args[0] == "item" -> {
            val item = sender.equipment.itemInMainHand
            if (item.itemMeta != null) {
              val im = item.itemMeta
              when {
                args[1].endsWith("int") -> {
                  im?.persistentDataContainer?.set(NamespacedKey.fromString(args[1])!!, PersistentDataType.INTEGER, args[2].toInt())
                }
                args[1].endsWith("str") -> {
                  im?.persistentDataContainer?.set(NamespacedKey.fromString(args[1])!!, PersistentDataType.STRING, args[2])
                }
              }
            }
          }
          args[0] == "player" -> {
            when {
              args[1].endsWith("int") -> {
                sender.persistentDataContainer.set(NamespacedKey.fromString(args[1])!!, PersistentDataType.INTEGER, args[2].toInt())
              }
              args[1].endsWith("str") -> {
                sender.persistentDataContainer.set(NamespacedKey.fromString(args[1])!!, PersistentDataType.STRING, args[2])
              }
            }
          }
        }
      } else {
        val msg = Component.text("Not enough or too many arguments!").color(NamedTextColor.RED)
        sender.sendMessage(msg)
      }
    }
    return true
  }

  override fun onTabComplete(sender: CommandSender, command: Command, label: String, args: Array<out String>): MutableList<String>? {
    if (args.size == 1) {
      val list = ArrayList<String>()
      list.add("hand")
      list.add("player")
      return list
    }
    return null
  }
}