package me.ixhbinphoenix.smPl.smCore.commands

import me.ixhbinphoenix.smPl.smCore.chat.getPlayerInfo
import me.ixhbinphoenix.smPl.smCore.chat.getRankColor
import me.ixhbinphoenix.smPl.smCore.utils.getPlayerRank
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.HoverEvent
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Bukkit
import org.bukkit.NamespacedKey
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.persistence.PersistentDataType

class scCommand : BaseCommand {
  override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
    if (sender is Player) {
      if (args.isEmpty()) {
        sender.sendMessage(
          Component.text("[SC] ").color(NamedTextColor.DARK_PURPLE).append(
            Component.text("Can't send an empty message!").color(
              NamedTextColor.RED
            )
          )
        )
      } else {
        if (args.size == 1 && args[0] == "toggle") {
          // We need a PersistentDataType.BOOLEAN
          val sctoggle = sender.persistentDataContainer.getOrDefault(
            NamespacedKey.fromString("smcore:chat.staff.toggle.str")!!,
            PersistentDataType.STRING,
            "true"
          )
          if (sctoggle == "true") {
            sender.persistentDataContainer.set(
              NamespacedKey.fromString("smcore:chat.staff.toggle.str")!!,
              PersistentDataType.STRING,
              "false"
            )
            sender.sendMessage(
              Component.text("[SC] ").color(NamedTextColor.DARK_PURPLE).append(
                Component.text("Toggled Staffchat off!").color(
                  NamedTextColor.RED
                )
              )
            )
          } else {
            sender.persistentDataContainer.set(
              NamespacedKey.fromString("smcore:chat.staff.toggle.str")!!,
              PersistentDataType.STRING,
              "true"
            )
            sender.sendMessage(
              Component.text("[SC] ").color(NamedTextColor.DARK_PURPLE).append(
                Component.text("Toggled Staffchat on!").color(
                  NamedTextColor.GREEN
                )
              )
            )
          }
        } else {
          if (sender.persistentDataContainer.getOrDefault(
              NamespacedKey.fromString("smcore:chat.staff.toggle.str")!!,
              PersistentDataType.STRING,
              "true"
            ) == "false"
          ) {
            sender.sendMessage(
              Component.text("[SC] ").color(NamedTextColor.DARK_PURPLE).append(
                Component.text("Staffchat is toggled off!").color(
                  NamedTextColor.RED
                )
              )
            )
          } else {
            var msg = Component.text("[SC] ").color(NamedTextColor.DARK_PURPLE)
              .append(Component.text(sender.name).color(getRankColor(getPlayerRank(sender))).hoverEvent(getPlayerInfo(sender)))
              .append(Component.text(" >> ").color(NamedTextColor.DARK_PURPLE))
            for (word in args) {
              msg = msg.append(Component.text("$word ").color(NamedTextColor.LIGHT_PURPLE))
            }
            for (player in Bukkit.getServer().onlinePlayers) {
              if (
                player.hasPermission("smcore.staffchat")
                && player.persistentDataContainer.getOrDefault(
                  NamespacedKey.fromString("smcore:chat.staff.toggle.str")!!,
                  PersistentDataType.STRING,
                  "true"
                ) == "true"
              ) {
                player.sendMessage(msg)
              }
            }
            Bukkit.getServer().consoleSender.sendMessage(msg)
          }
        }
      }
    } else {
      var msg = Component.text("[SC] ").color(NamedTextColor.DARK_PURPLE)
        .append(Component.text(sender.name).color(NamedTextColor.DARK_RED))
        .append(Component.text(" >> ").color(NamedTextColor.DARK_PURPLE))
      for (word in args) {
        msg = msg.append(Component.text("$word ").color(NamedTextColor.LIGHT_PURPLE))
      }
      for (player in Bukkit.getServer().onlinePlayers) {
        if (
          player.hasPermission("smcore.staffchat")
          && player.persistentDataContainer.getOrDefault(
            NamespacedKey.fromString("smcore:chat.staff.toggle.str")!!,
            PersistentDataType.STRING,
            "true"
          ) == "true"
        ) {
          player.sendMessage(msg)
        }
      }
      Bukkit.getServer().consoleSender.sendMessage(msg)
    }
    return true
  }

  override fun onTabComplete(sender: CommandSender, command: Command, label: String, args: Array<out String>): MutableList<String>? {
    if (args.size == 1 && sender is Player && sender.hasPermission("smcore.staffchat")) {
      val list = ArrayList<String>()
      list.add("toggle")
      return list
    }
    return null
  }
}