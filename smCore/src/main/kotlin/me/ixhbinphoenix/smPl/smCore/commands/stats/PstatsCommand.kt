package me.ixhbinphoenix.smPl.smCore.commands.stats

import me.ixhbinphoenix.smPl.smCore.commands.BaseCommand
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.persistence.PersistentDataType

class pstatsCommand : BaseCommand {
  override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
    if (sender is Player) {
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
    return true
  }

  override fun onTabComplete(sender: CommandSender, command: Command, label: String, args: Array<out String>): MutableList<String>? {
    return null
  }
}