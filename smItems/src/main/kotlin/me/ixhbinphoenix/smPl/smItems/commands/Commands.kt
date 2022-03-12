package me.ixhbinphoenix.smPl.smItems.commands

import me.ixhbinphoenix.smPl.smItems.Main
import net.md_5.bungee.api.ChatColor
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.event.Listener
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.persistence.PersistentDataType

class Commands : CommandExecutor,Listener {
  val cmds = ArrayList<String>();
  init {
    cmds.add("meta")
    cmds.add("customitem")
  }
  override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
    val plugin = Bukkit.getPluginManager().getPlugin("smItems") as Main
    if (sender is Player) {
      when {
        command.name.lowercase() == "meta" -> {
          val item = sender.inventory.getItem(sender.inventory.heldItemSlot) as ItemStack
          if (item.hasItemMeta()) {
            val im = item.itemMeta
            sender.sendMessage(ChatColor.GOLD.toString() + "psdc info for " + ChatColor.RED.toString() + im?.displayName)
            for (key in im?.persistentDataContainer?.keys!!) {
              var keyVal: Any = -2;
              when {
                key.toString().endsWith("int") -> {
                  keyVal = im.persistentDataContainer.getOrDefault(key, PersistentDataType.INTEGER, -1)
                }
                key.toString().endsWith("str") -> {
                  keyVal = im.persistentDataContainer.getOrDefault(key, PersistentDataType.STRING, "-1")
                }
              }
              sender.sendMessage(ChatColor.GOLD.toString() + key.toString() + " = " + keyVal.toString())
            }
          } else {
            sender.sendMessage(ChatColor.RED.toString() + "Item " + item.type + " does not have any item meta")
          }
        }
        command.name.lowercase() == "customitem" -> {
          when {
            args[0] == "satans_teachings" -> {
              val item = ItemStack(Material.BOOK, 1)
              val im = item.itemMeta as ItemMeta
              im.setDisplayName(ChatColor.RED.toString() + "Satan's teachings")
              val lore = ArrayList<String>()
              lore.add(ChatColor.GRAY.toString() + "Damage: " + ChatColor.RED +"666")
              im.persistentDataContainer.set(NamespacedKey.fromString("weapon.damage.int", plugin)!!, PersistentDataType.INTEGER, 666)
              lore.add(ChatColor.GRAY.toString() + "Mana: " + ChatColor.AQUA + "420")
              im.persistentDataContainer.set(NamespacedKey.fromString("weapon.mana.int", plugin)!!, PersistentDataType.INTEGER, 420)
              lore.add("")
              lore.add(ChatColor.RED.toString() + "LEGENDARY BOOK")
              im.persistentDataContainer.set(NamespacedKey.fromString("item.rarity.str", plugin)!!, PersistentDataType.STRING, "LEGENDARY")
              im.persistentDataContainer.set(NamespacedKey.fromString("item.type.str", plugin)!!, PersistentDataType.STRING, "WEAPON")
              im.persistentDataContainer.set(NamespacedKey.fromString("weapon.type.str", plugin)!!, PersistentDataType.STRING, "BOOK")
              im.lore = lore
              item.itemMeta = im
              sender.inventory.addItem(item)
            }
          }
        }
      }
    }
    return true;
  }
}