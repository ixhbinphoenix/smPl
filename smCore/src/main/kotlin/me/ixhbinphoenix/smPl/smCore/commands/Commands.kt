package me.ixhbinphoenix.smPl.smCore

import net.md_5.bungee.api.ChatColor
import org.bukkit.NamespacedKey
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.event.Listener
import org.bukkit.entity.Player
import org.bukkit.persistence.PersistentDataType

class Commands : CommandExecutor,Listener {
  val cmds = ArrayList<String>();
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
          sender.sendMessage(ChatColor.GREEN.toString() + "Pong!");
        }
        command.name.lowercase() == "pstats" -> {
          sender.sendMessage(ChatColor.GOLD.toString() + "psdc info for you:")
          for (key in sender.persistentDataContainer.keys) {
            var keyVal: Any = -2;
            when {
              key.toString().endsWith("int") -> {
                keyVal = sender.persistentDataContainer.getOrDefault(key, PersistentDataType.INTEGER, -1)
              }
              key.toString().endsWith("str") -> {
                keyVal = sender.persistentDataContainer.getOrDefault(key, PersistentDataType.STRING, "-1")
              }
            }
            sender.sendMessage(ChatColor.GOLD.toString() + key.toString() + " = " + keyVal.toString())
          }
        }
        command.name.lowercase() == "setstat" -> {
          if (args.size == 3) {
            when {
              args[0] == "item" -> {
                val item = sender.equipment?.itemInMainHand
                if (item != null && item.itemMeta != null) {
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
            sender.sendMessage(ChatColor.RED.toString() + "Not enough or too many arguments")
          }
        }
        command.name.lowercase() == "delstat" -> {
          if (args.size == 1) {
            sender.persistentDataContainer.remove(NamespacedKey.fromString(args[0])!!)
          } else {
            sender.sendMessage(ChatColor.RED.toString() + "Not enough or too many arguments")
          }
        }
      }
    }
    return true;
  }
}