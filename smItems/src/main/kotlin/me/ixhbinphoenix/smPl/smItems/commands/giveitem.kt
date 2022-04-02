package me.ixhbinphoenix.smPl.smItems.commands

import me.ixhbinphoenix.smPl.smCore.commands.BaseCommand
import me.ixhbinphoenix.smPl.smItems.*
import me.ixhbinphoenix.smPl.smItems.item.ItemUtils
import me.ixhbinphoenix.smPl.smItems.item.SetBonus
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.meta.LeatherArmorMeta
import kotlin.collections.ArrayList

class giveItemCommand : BaseCommand {
  private val itemUtils = ItemUtils()

  override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
    if (sender is Player) {
      when {
        args[0] == "satans_teachings" -> {
          val item = itemUtils.createWeapon(Material.BOOK, "Satan's teachings", "satans_teachings", Rarity.LEGENDARY, WeaponTypes.BOOK, 666, 420, null)
          itemUtils.giveItem(item, sender)
        }
        args[0] == "programmers_cat_ears" -> {
          val set = SetBonus
          set.bonusName = "Programmer"
          set.set = "PROGRAMMER"

          val setLore = ArrayList<Component>()
          setLore.add(Component.text("The ultimate outfit for a Programmer").color(NamedTextColor.DARK_GRAY))
          set.setLore = setLore

          val effect = ArrayList<Component>()
          effect.add(Component.text("Gain 10,000 Mana").color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false))
          set.setEffect = effect

          val item = itemUtils.createArmor(Material.LEATHER_HELMET, "Programmer's Cat ears", "programmers_cat_ears", Rarity.MYTHIC, ArmorTypes.HELMET, 1000, 1000, set)
          val im = item.itemMeta as LeatherArmorMeta
          im.setColor(Color.FUCHSIA)
          im.isUnbreakable = true
          item.itemMeta = im
          itemUtils.giveItem(item, sender)
        }
        args[0] == "programmers_croptop" -> {
          val set = SetBonus
          set.bonusName = "Programmer"
          set.set = "PROGRAMMER"

          val setLore = ArrayList<Component>()
          setLore.add(Component.text("The ultimate outfit for a Programmer").color(NamedTextColor.DARK_GRAY))
          set.setLore = setLore

          val effect = ArrayList<Component>()
          effect.add(Component.text("Gain 10,000 Mana").color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false))
          set.setEffect = effect

          val item = itemUtils.createArmor(Material.LEATHER_CHESTPLATE, "Programmer's Crop-top", "programmers_crop_top", Rarity.MYTHIC, ArmorTypes.CHESTPLATE, 1000, 1000, set)
          val im = item.itemMeta as LeatherArmorMeta
          im.setColor(Color.FUCHSIA)
          im.isUnbreakable = true
          item.itemMeta = im
          itemUtils.giveItem(item, sender)
        }
        args[0] == "programmers_skirt" -> {
          val set = SetBonus
          set.bonusName = "Programmer"
          set.set = "PROGRAMMER"

          val setLore = ArrayList<Component>()
          setLore.add(Component.text("The ultimate outfit for a Programmer").color(NamedTextColor.DARK_GRAY))
          set.setLore = setLore

          val effect = ArrayList<Component>()
          effect.add(Component.text("Gain 10,000 Mana").color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false))
          set.setEffect = effect

          val item = itemUtils.createArmor(Material.LEATHER_LEGGINGS, "Programmer's Skirt", "programmers_skirt", Rarity.MYTHIC, ArmorTypes.LEGGINGS, 1000, 1000, set)
          val im = item.itemMeta as LeatherArmorMeta
          im.setColor(Color.FUCHSIA)
          im.isUnbreakable = true
          item.itemMeta = im
          itemUtils.giveItem(item, sender)
        }
        args[0] == "programmers_thigh_highs" -> {
          val set = SetBonus
          set.bonusName = "Programmer"
          set.set = "PROGRAMMER"

          val setLore = ArrayList<Component>()
          setLore.add(Component.text("The ultimate outfit for a Programmer").color(NamedTextColor.DARK_GRAY))
          set.setLore = setLore

          val effect = ArrayList<Component>()
          effect.add(Component.text("Gain 10,000 Mana").color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false))
          set.setEffect = effect

          val item = itemUtils.createArmor(Material.LEATHER_BOOTS, "Programmer's Thigh-highs", "programmers_thigh_highs", Rarity.MYTHIC, ArmorTypes.BOOTS, 1000, 1000, set)
          val im = item.itemMeta as LeatherArmorMeta
          im.setColor(Color.FUCHSIA)
          im.isUnbreakable = true
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
      return arrayListOf(
        "satans_teachings",
        "programmers_cat_ears",
        "programmers_croptop",
        "programmers_skirt",
        "programmers_thigh_highs"
      )
    }
    return null
  }

}