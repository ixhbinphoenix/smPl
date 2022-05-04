package me.ixhbinphoenix.smPl.smItems.commands

import me.ixhbinphoenix.smPl.smCore.commands.BaseCommand
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.persistence.PersistentDataType

class metaCommand : BaseCommand {
  override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
    if (sender is Player) {
      val item = sender.inventory.getItem(sender.inventory.heldItemSlot)
      if (item is ItemStack) {
        if (item.hasItemMeta()) {
          val im = item.itemMeta as ItemMeta
          var msg = Component.text("psdc info for ").color(NamedTextColor.GOLD)
            .append(im.displayName()!!.color(NamedTextColor.RED))
          for (key in im.persistentDataContainer.keys) {
            var keyVal: Any = -2
            when {
              key.toString().endsWith("int") -> {
                keyVal = im.persistentDataContainer.getOrDefault(key, PersistentDataType.INTEGER, -1)
              }
              key.toString().endsWith("str") -> {
                keyVal = im.persistentDataContainer.getOrDefault(key, PersistentDataType.STRING, "-1")
              }
            }
            msg = msg.append(Component.text("\n$key = $keyVal").color(NamedTextColor.GOLD))
          }
          sender.sendMessage(msg)
        } else {
          val msg = Component.text("Item " + item.type + " does not have any item meta").color(NamedTextColor.RED)
          sender.sendMessage(msg)
        }
      } else {
        val msg = Component.text("You don't have an item in your main hand!").color(NamedTextColor.RED)
        sender.sendMessage(msg)
      }
    }
    return true
  }

  override fun onTabComplete(sender: CommandSender, command: Command, label: String, args: Array<out String>): MutableList<String>? {
    if (args.size == 1) {
      val list = ArrayList<String>()
      list.add("self")
      list.add("reset")
      return list
    }
    return null
  }
}