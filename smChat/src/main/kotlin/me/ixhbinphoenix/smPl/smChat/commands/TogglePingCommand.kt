package me.ixhbinphoenix.smPl.smChat.commands

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.NamespacedKey
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.persistence.PersistentDataType

class TogglePingCommand : BaseCommand {
  override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
    if (sender is Player) {
      val pingtoggle = sender.persistentDataContainer.getOrDefault(NamespacedKey.fromString("smchat:ping.toggle.str")!!, PersistentDataType.STRING, "true")
      if (pingtoggle == "true") {
        sender.persistentDataContainer.set(NamespacedKey.fromString("smchat:ping.toggle.str")!!, PersistentDataType.STRING, "false")
        sender.sendMessage(Component.text("Disabled chat ping sound!").color(NamedTextColor.RED))
      } else {
        sender.persistentDataContainer.set(NamespacedKey.fromString("smchat:ping.toggle.str")!!, PersistentDataType.STRING, "true")
        sender.sendMessage(Component.text("Enabled chat ping sound!").color(NamedTextColor.GREEN))
      }
    }
    return true
  }

  override fun onTabComplete(sender: CommandSender, command: Command, label: String, args: Array<out String>): MutableList<String>? {
    return null
  }
}