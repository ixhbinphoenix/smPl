package me.ixhbinphoenix.smPl.smCore.commands

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.NamespacedKey
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.event.Listener
import org.bukkit.entity.Player
import org.bukkit.persistence.PersistentDataType

class Commands : CommandExecutor,Listener {
  val cmds = ArrayList<String>()
  init {
    cmds.add("ping")
    cmds.add("pstats")
    cmds.add("setstat")
    cmds.add("delstat")
  }
  override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
    if (sender is Player) {
      when {
        command.name.lowercase() == "ping" -> {
          val msg: TextComponent = Component.text("Pong!").color(NamedTextColor.GREEN)
          sender.sendMessage(msg)
        }
        command.name.lowercase() == "pstats" -> {
          var msg: TextComponent = Component.text("psdc info for you:").color(NamedTextColor.GOLD)
          for (key in sender.persistentDataContainer.keys) {
            var keyVal: Any = -2
            when {
              key.toString().endsWith("int") -> {
                keyVal = sender.persistentDataContainer.getOrDefault(key, PersistentDataType.INTEGER, -1)
              }
              key.toString().endsWith("str") -> {
                keyVal = sender.persistentDataContainer.getOrDefault(key, PersistentDataType.STRING, "-1")
              }
            }
            msg = msg.append(Component.text("\n$key = $keyVal").color(NamedTextColor.GOLD))
          }
          sender.sendMessage(msg)
        }
        command.name.lowercase() == "setstat" -> {
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
        command.name.lowercase() == "delstat" -> {
          if (args.size == 1) {
            sender.persistentDataContainer.remove(NamespacedKey.fromString(args[0])!!)
          } else {
            val msg = Component.text("Not enough or too many arguments!").color(NamedTextColor.RED)
            sender.sendMessage(msg)
          }
        }
      }
    }
    return true
  }
}