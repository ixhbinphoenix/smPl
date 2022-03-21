package me.ixhbinphoenix.smPl.smItems.commands

import me.ixhbinphoenix.smPl.smCore.player.PlayerHandler
import me.ixhbinphoenix.smPl.smItems.Main
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
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
  val cmds = ArrayList<String>()
  init {
    cmds.add("meta")
    cmds.add("customitem")
  }
  override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
    val plugin = Bukkit.getPluginManager().getPlugin("smItems") as Main
    if (sender is Player) {
      when {
        command.name.lowercase() == "meta" -> {
          if(args.size == 1){
            if(args[0] == "self"){
              val player = sender
              val playerHandler = PlayerHandler(player)
              val message = Component.text("psdc info for ").color(NamedTextColor.GOLD)
                .append(player.name().color(NamedTextColor.RED))
                .append(Component.text(
                  "\nDamage: ${playerHandler.getDamage()}" +
                          "\nMana: ${playerHandler.getMana()}" +
                          "\n"
                ).color(NamedTextColor.GOLD))
              player.sendMessage(message)
            }
          }
          val item = sender.inventory.getItem(sender.inventory.heldItemSlot) as ItemStack
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
        }
        command.name.lowercase() == "customitem" -> {
          when {
            args[0] == "satans_teachings" -> {
              val item = ItemStack(Material.BOOK, 1)
              val im = item.itemMeta as ItemMeta
              im.displayName(Component.text("Satan's teachings").color(NamedTextColor.RED).decoration(TextDecoration.ITALIC, false))
              val lore = ArrayList<Component>()
              lore.add(Component.text("Damage: ").color(NamedTextColor.GRAY)
                .append(Component.text("666").color(NamedTextColor.RED)).decoration(TextDecoration.ITALIC, false))
              im.persistentDataContainer.set(NamespacedKey.fromString("weapon.damage.int", plugin)!!, PersistentDataType.INTEGER, 666)
              lore.add(Component.text("Mana: ").color(NamedTextColor.GRAY)
                .append(Component.text("420").color(NamedTextColor.AQUA)).decoration(TextDecoration.ITALIC, false))
              im.persistentDataContainer.set(NamespacedKey.fromString("weapon.mana.int", plugin)!!, PersistentDataType.INTEGER, 420)
              lore.add(Component.text(""))
              lore.add(Component.text("LEGENDARY BOOK").color(NamedTextColor.RED).decoration(TextDecoration.ITALIC, false))
              im.persistentDataContainer.set(NamespacedKey.fromString("item.rarity.str", plugin)!!, PersistentDataType.STRING, "LEGENDARY")
              im.persistentDataContainer.set(NamespacedKey.fromString("item.type.str", plugin)!!, PersistentDataType.STRING, "WEAPON")
              im.persistentDataContainer.set(NamespacedKey.fromString("weapon.type.str", plugin)!!, PersistentDataType.STRING, "BOOK")
              im.lore(lore)
              item.itemMeta = im
              sender.inventory.addItem(item)
            }
          }
        }
      }
    }
    return true
  }
}