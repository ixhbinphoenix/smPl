package me.ixhbinphoenix.smPl.smItems.commands

import me.ixhbinphoenix.smPl.smCore.chat.createStatText
import me.ixhbinphoenix.smPl.smCore.commands.BaseCommand
import me.ixhbinphoenix.smPl.smItems.RarityColor
import me.ixhbinphoenix.smPl.smItems.getInstance
import me.ixhbinphoenix.smPl.smItems.item.ItemUtils
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.inventory.meta.LeatherArmorMeta
import org.bukkit.persistence.PersistentDataType
import java.util.*
import kotlin.collections.ArrayList

class giveItemCommand : BaseCommand {
  private val itemUtils = ItemUtils()
  private val plugin = getInstance()

  override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
    if (sender is Player) {
      when {
        args[0] == "satans_teachings" -> {
          val item = ItemStack(Material.BOOK, 1)
          val im = item.itemMeta as ItemMeta
          im.displayName(
            Component.text("Satan's teachings").color(RarityColor.LEGENDARY.color).decoration(
              TextDecoration.ITALIC, false))
          val lore = ArrayList<Component>()
          lore.add(
            Component.text("Damage: ").color(NamedTextColor.GRAY)
            .append(Component.text("666").color(NamedTextColor.RED)).decoration(TextDecoration.ITALIC, false))
          im.persistentDataContainer.set(NamespacedKey.fromString("weapon.damage.int", plugin)!!, PersistentDataType.INTEGER, 666)
          lore.add(
            Component.text("Mana: ").color(NamedTextColor.GRAY)
            .append(Component.text("420").color(NamedTextColor.AQUA)).decoration(TextDecoration.ITALIC, false))
          im.persistentDataContainer.set(NamespacedKey.fromString("weapon.mana.int", plugin)!!, PersistentDataType.INTEGER, 420)
          lore.add(Component.text(""))
          lore.add(Component.text("LEGENDARY BOOK").color(RarityColor.LEGENDARY.color).decoration(TextDecoration.ITALIC, false))
          im.persistentDataContainer.set(NamespacedKey.fromString("item.rarity.str", plugin)!!, PersistentDataType.STRING, "LEGENDARY")
          im.persistentDataContainer.set(NamespacedKey.fromString("item.type.str", plugin)!!, PersistentDataType.STRING, "WEAPON")
          im.persistentDataContainer.set(NamespacedKey.fromString("weapon.type.str", plugin)!!, PersistentDataType.STRING, "BOOK")
          im.persistentDataContainer.set(NamespacedKey.fromString("item.id.str")!!, PersistentDataType.STRING, "satans_teachings")
          im.persistentDataContainer.set(NamespacedKey.fromString("item.uuid.str")!!, PersistentDataType.STRING, UUID.randomUUID().toString())
          im.lore(lore)
          item.itemMeta = im
          itemUtils.giveItem(item, sender)
        }
        args[0] == "cat_ears" -> {
          val item = ItemStack(Material.LEATHER_HELMET, 1)
          val im = item.itemMeta as LeatherArmorMeta
          im.isUnbreakable = true
          im.displayName(Component.text("Cat ears").color(RarityColor.MYTHIC.color).decoration(TextDecoration.ITALIC, false))
          im.setColor(Color.FUCHSIA)
          val lore = ArrayList<Component>()
          lore.add(createStatText("Defence", "69", NamedTextColor.GREEN, false).decoration(TextDecoration.ITALIC, false))
          im.persistentDataContainer.set(NamespacedKey.fromString("smitems:armor.defence.int")!!, PersistentDataType.INTEGER, 69)
          lore.add(createStatText("Max Health", "727", NamedTextColor.RED, false).decoration(TextDecoration.ITALIC, false))
          im.persistentDataContainer.set(NamespacedKey.fromString("smitems:armor.maxhealth.int")!!, PersistentDataType.INTEGER, 727)
          lore.add(Component.text(""))
          lore.add(Component.text("MYTHIC HELMET").color(RarityColor.MYTHIC.color).decoration(TextDecoration.ITALIC, false))
          im.persistentDataContainer.set(NamespacedKey.fromString("smitems:item.rarity.str")!!, PersistentDataType.STRING, "MYTHIC")
          im.persistentDataContainer.set(NamespacedKey.fromString("smitems:item.type.str")!!, PersistentDataType.STRING, "ARMOR")
          im.persistentDataContainer.set(NamespacedKey.fromString("smitems:armor.type.str")!!, PersistentDataType.STRING, "HELMET")
          im.persistentDataContainer.set(NamespacedKey.fromString("smitems:item.id.str")!!, PersistentDataType.STRING, "cat_ears")
          im.persistentDataContainer.set(NamespacedKey.fromString("smitems:item.uuid.str")!!, PersistentDataType.STRING, UUID.randomUUID().toString())
          im.lore(lore)
          im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES)
          im.addItemFlags(ItemFlag.HIDE_DYE)
          im.addItemFlags(ItemFlag.HIDE_UNBREAKABLE)
          item.itemMeta = im
          itemUtils.giveItem(item, sender)
        }
        else -> {
          sender.sendMessage(Component.text("Unknown Item!").color(NamedTextColor.RED))
        }
      }
    }
    return true
  }

  override fun onTabComplete(sender: CommandSender, command: Command, label: String, args: Array<out String>): MutableList<String>? {
    if (args.size == 1) {
      val items = arrayListOf(
        "satans_teachings",
        "cat_ears"
      )
      return items
    }
    return null
  }

}