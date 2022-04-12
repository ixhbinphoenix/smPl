package me.ixhbinphoenix.smPl.smItems.item

import me.ixhbinphoenix.smPl.smChat.utils.createProgressBar
import me.ixhbinphoenix.smPl.smChat.utils.createStatText
import me.ixhbinphoenix.smPl.smItems.*
import me.ixhbinphoenix.smPl.smItems.events.StatsCalculation
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import org.bukkit.scheduler.BukkitRunnable
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.roundToInt
import kotlin.math.floor
import kotlin.math.pow

object SetBonus {
  var set: String = "NONE"
  var bonusName: String = "Unknown"
  var setLore: ArrayList<Component>? = null
  var setEffect: ArrayList<Component> = arrayListOf(Component.text("Has no effect!"))
}

class LoreRefresh(private val event: InventoryClickEvent) : BukkitRunnable() {
  override fun run() {
    val player = event.whoClicked as Player
    val slot = event.slot
    if (player.inventory.getItem(slot) is ItemStack && player.inventory.getItem(slot)!!.hasItemMeta()) {
      val item = player.inventory.getItem(slot)!!
      val handler = ItemHandler(item, player)
      handler.updateLore()
    }
    if (event.currentItem is ItemStack && event.currentItem!!.hasItemMeta()) {
      val item = event.currentItem!!
      val handler = ItemHandler(item, player)
      handler.updateLore()
    }
  }
}

class ItemUtils {
  private val plugin: Main = getInstance()

  fun giveItem(item: ItemStack, player: Player) {
    player.inventory.addItem(item)
    StatsCalculation(player).runTaskLater(plugin, 2)
  }

  fun createWeapon(mat: Material, name: String, id: String, rarity: Rarity, weaponType: WeaponTypes, dmg: Int, mana: Int = 0, set: SetBonus?): ItemStack {
    val item = ItemStack(mat, 1)
    val im = item.itemMeta
    im.isUnbreakable = true
    im.displayName(Component.text(name).color(RarityColor.valueOf(rarity.name).color).decoration(TextDecoration.ITALIC, false))
    val statTexts = ArrayList<Component>()
    statTexts.add(createStatText("Damage", dmg.toString(), NamedTextColor.RED, false).decoration(TextDecoration.ITALIC, false))
    im.persistentDataContainer.set(NamespacedKey.fromString("smitems:equipment.damage.int")!!, PersistentDataType.INTEGER, dmg)
    if (mana > 0) {
      statTexts.add(createStatText("Mana", mana.toString(), NamedTextColor.AQUA, false).decoration(TextDecoration.ITALIC, false))
      im.persistentDataContainer.set(NamespacedKey.fromString("smitems:equipment.mana.int")!!, PersistentDataType.INTEGER, mana)
    }
    im.persistentDataContainer.set(NamespacedKey.fromString("smitems:item.rarity.str")!!, PersistentDataType.STRING, rarity.name)
    im.persistentDataContainer.set(NamespacedKey.fromString("smitems:item.type.str")!!, PersistentDataType.STRING, ItemCategories.WEAPON.name)
    im.persistentDataContainer.set(NamespacedKey.fromString("smitems:weapon.type.str")!!, PersistentDataType.STRING, weaponType.name)
    im.persistentDataContainer.set(NamespacedKey.fromString("smitems:item.id.str")!!, PersistentDataType.STRING, id)
    im.persistentDataContainer.set(NamespacedKey.fromString("smitems:item.uuid.str")!!, PersistentDataType.STRING, UUID.randomUUID().toString())
    im.lore(genLore(rarity, Types.valueOf(weaponType.name), statTexts, 0, 0, set))
    im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES)
    im.addItemFlags(ItemFlag.HIDE_UNBREAKABLE)
    im.addItemFlags(ItemFlag.HIDE_DYE)
    item.itemMeta = im
    return item
  }

  fun createArmor(mat: Material, name: String, id: String, rarity: Rarity, armorType: ArmorTypes, defence: Int = 0, maxHealth: Int = 0, set: SetBonus?): ItemStack {
    val item = ItemStack(mat, 1)
    val im = item.itemMeta
    im.isUnbreakable = true
    im.displayName(Component.text(name).color(RarityColor.valueOf(rarity.name).color).decoration(TextDecoration.ITALIC, false))
    val statTexts = ArrayList<Component>()
    if (defence > 0) {
      statTexts.add(createStatText("Defence", defence.toString(), NamedTextColor.GREEN, false).decoration(TextDecoration.ITALIC, false))
      im.persistentDataContainer.set(NamespacedKey.fromString("smitems:equipment.defence.int")!!, PersistentDataType.INTEGER, defence)
    }
    if (maxHealth > 0) {
      statTexts.add(createStatText("Max Health", maxHealth.toString(), NamedTextColor.RED, false).decoration(TextDecoration.ITALIC, false))
      im.persistentDataContainer.set(NamespacedKey.fromString("smitems:equipment.maxhealth.int")!!, PersistentDataType.INTEGER, maxHealth)
    }
    if (set is SetBonus) {
      im.persistentDataContainer.set(NamespacedKey.fromString("smitems:item.set.str")!!, PersistentDataType.STRING, set.set)
    }
    im.persistentDataContainer.set(NamespacedKey.fromString("smitems:item.rarity.str")!!, PersistentDataType.STRING, rarity.name)
    im.persistentDataContainer.set(NamespacedKey.fromString("smitems:item.type.str")!!, PersistentDataType.STRING, ItemCategories.ARMOR.name)
    im.persistentDataContainer.set(NamespacedKey.fromString("smitems:armor.type.str")!!, PersistentDataType.STRING, armorType.name)
    im.persistentDataContainer.set(NamespacedKey.fromString("smitems:item.id.str")!!, PersistentDataType.STRING, id)
    im.persistentDataContainer.set(NamespacedKey.fromString("smitems:item.uuid.str")!!, PersistentDataType.STRING, UUID.randomUUID().toString())
    im.lore(genLore(rarity, Types.valueOf(armorType.name), statTexts, 0, 0, set))
    im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES)
    im.addItemFlags(ItemFlag.HIDE_UNBREAKABLE)
    im.addItemFlags(ItemFlag.HIDE_DYE)
    item.itemMeta = im
    return item
  }

  fun genLore(rarity: Rarity, type: Types, stats: ArrayList<Component>, xp: Int, setCompletion: Int = 0, set: SetBonus?): ArrayList<Component> {
    val lore = ArrayList<Component>()
    lore.addAll(stats)
    lore.add(Component.text("").decoration(TextDecoration.ITALIC, false))
    lore.add(Component.text("Level: ").color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false))
    val level = calcRarityLevel(xp, getRarityMultiplier(rarity))
    val percent = "%.2f".format((level - floor(level)) * 100).toDouble()
    var levelprog = Component.text(floor(level).roundToInt()).color(NamedTextColor.GREEN)
    levelprog = if (floor(level).roundToInt() != 20) {
      levelprog.append(
        Component.text(" → ").color(NamedTextColor.GRAY)
          .append(Component.text(floor(level).roundToInt() + 1).color(NamedTextColor.DARK_GREEN))
          .append(Component.text(" "))
          .append(createProgressBar(20, (level - floor(level)) * 100))
          .append(Component.text(" ${percent}%").color(NamedTextColor.GREEN))
      )
    } else {
      levelprog.append(
        Component.text(" MAX!").color(NamedTextColor.GREEN).decoration(TextDecoration.BOLD, true)
      )
    }
    lore.add(levelprog.decoration(TextDecoration.ITALIC, false))
    lore.add(Component.text(""))
    if (set is SetBonus) {
      if (setCompletion == 4) {
        lore.add(Component.text("Set Bonus: ${set.bonusName}").color(NamedTextColor.GOLD).append(Component.text(" ($setCompletion/4)").color(NamedTextColor.GREEN)).decoration(TextDecoration.ITALIC, false))
      } else {
        lore.add(Component.text("Set Bonus: ${set.bonusName}").color(NamedTextColor.GOLD).append(Component.text(" ($setCompletion/4)").color(NamedTextColor.DARK_GRAY)).decoration(TextDecoration.ITALIC, false))
      }
      if (set.setLore is ArrayList<Component> && set.setLore!!.size > 0) {
        lore.addAll(set.setLore!!)
      }
      lore.addAll(set.setEffect)
      lore.add(Component.text(""))
    }
    lore.add(Component.text("${rarity.name} ${type.name}").color(RarityColor.valueOf(rarity.name).color).decoration(TextDecoration.ITALIC, false))
    return lore
  }

  fun getRarityMultiplier(rarity: Rarity): Int {
    return when (rarity) {
      Rarity.COMMON -> 500
      Rarity.UNCOMMON -> 750
      Rarity.RARE -> 1500
      Rarity.EPIC -> 2250
      Rarity.LEGENDARY -> 3500
      Rarity.MYTHIC -> 5000
    }
  }

  // this is a terrible way of doing this
  fun calcRarityLevel(xp: Int, a: Int): Double {
    // These ARE constants that could have been pre-calculated, however that would look ugly as fuck and it takes
    // literal nanoseconds to calculate this (i hope lmao)
    if (xp < 1) return 0.0
    for (x in 0..19) {
      val fraction = xp / (a * x.toDouble().pow(2))
      if (x == 19 && fraction > 1) return 20.0
      if (fraction > 1) continue
      return fraction + x - 1
    }
    throw IllegalStateException("calcRarityLevel could not calculate level and exited without returning")
  }
}