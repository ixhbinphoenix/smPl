package me.ixhbinphoenix.smPl.smCore.items

import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent
import me.ixhbinphoenix.smPl.smChat.utils.createProgressBar
import me.ixhbinphoenix.smPl.smChat.utils.createStatText
import me.ixhbinphoenix.smPl.smCore.Main
import me.ixhbinphoenix.smPl.smCore.getInstance
import me.ixhbinphoenix.smPl.smCore.items.*
import me.ixhbinphoenix.smPl.smCore.items.abilities.Abilities
import me.ixhbinphoenix.smPl.smCore.items.abilities.AbilityDescription
import me.ixhbinphoenix.smPl.smCore.items.abilities.AbilityHandler
import me.ixhbinphoenix.smPl.smCore.items.abilities.genAbilityComponent
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import java.util.UUID
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.persistence.PersistentDataType
import org.bukkit.scheduler.BukkitRunnable
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.math.roundToInt
import kotlin.math.floor
import kotlin.math.pow

// Boni is a terrible plural for bonus
/**
 * Description of set boni
 * @property set ID of the set
 * @property bonusName Name of the set bonus
 * @property setLore Lore text
 * @property setEffect Description of the effect
 */
object SetBonus {
  var set: String = "NONE"
  var bonusName: String = "Unknown"
  var setLore: ArrayList<Component>? = null
  var setEffect: ArrayList<Component> = arrayListOf(Component.text("Has no effect!"))
}

/**
 * BukkitRunnable that refreshes Items during a ClickEvent
 */
class ClickLoreRefresh(private val event: InventoryClickEvent) : BukkitRunnable() {
  override fun run() {
    val player = event.whoClicked as Player
    val slot = event.slot
    if (player.inventory.getItem(slot) is ItemStack && player.inventory.getItem(slot)!!.hasItemMeta()) {
      val item = player.inventory.getItem(slot)!!
      val handler = EquipmentHandler(item, player)
      handler.updateLore()
    }
    if (event.currentItem is ItemStack && event.currentItem!!.hasItemMeta()) {
      val item = event.currentItem!!
      val handler = EquipmentHandler(item, player)
      handler.updateLore()
    }
  }
}

/**
 * BukkitRunnable that refreshes Armor during a ArmorChangeEvent
 */
class ArmorLoreRefresh(private val event: PlayerArmorChangeEvent) : BukkitRunnable() {
  @Suppress("SAFE_CALL_WILL_CHANGE_NULLABILITY")
  override fun run() {
    val armorSlots = event.player.inventory.armorContents
    // Ignore your IDE here, the compiler will complain without the safe call
    armorSlots?.forEach { armor ->
      (
              if (armor != null) {
                EquipmentHandler(armor, event.player).updateLore()
              }
      )
    }
  }
}

class ItemUtils {
  private val plugin: Main = getInstance()

  /**
   * Gives items to the Player and re-calculates their stats
   * @param item Item given to the Player
   * @param player
   */
  fun giveItem(item: ItemStack, player: Player) {
    player.inventory.addItem(item)
    StatsCalculation(player).runTaskLater(plugin, 2)
  }

  companion object {

    /**
     * Creates an Equipment Item
     * @param mat Actual minecraft Item
     * @param name Name
     * @param id Custom ID
     * @param rarity Rarity
     * @param Type Type of Equipment
     * @param stats Stats of the Item
     * @param element Element of the Equipment
     * @param set Set of the Equipment
     */
    fun createEquipment(
      mat: Material,
      name: String,
      id: String,
      rarity: Rarity,
      Type: EquipmentTypes,
      stats: HashMap<String, Int>,
      element: Elements?,
      set: SetBonus?
    ): ItemStack {
      val item = ItemStack(mat, 1)
      val im = setStats(stats, item.itemMeta)
      im.isUnbreakable = true
      im.displayName(Component.text(name).color(rarity.color).decoration(TextDecoration.ITALIC, false))
      if (element is Elements) {
        im.displayName(
          im.displayName()!!.append(Component.text(" ")).append(
            element.comp
          )
        )
      }
      val statText = genStatTexts(stats)
      im.persistentDataContainer.set(
        NamespacedKey.fromString("smcore:item.rarity.str")!!,
        PersistentDataType.STRING,
        rarity.name
      )
      im.persistentDataContainer.set(
        NamespacedKey.fromString("smcore:item.type.str")!!,
        PersistentDataType.STRING,
        Type.category.name
      )
      when (Type.category) {
        EquipmentCategories.WEAPON -> {
          im.persistentDataContainer.set(
            NamespacedKey.fromString("smcore:weapon.type.str")!!,
            PersistentDataType.STRING,
            Type.name
          )
        }

        EquipmentCategories.ARMOR -> {
          im.persistentDataContainer.set(
            NamespacedKey.fromString("smcore:armor.type.str")!!,
            PersistentDataType.STRING,
            Type.name
          )
        }

        EquipmentCategories.ACCESSORY -> {
          im.persistentDataContainer.set(
            NamespacedKey.fromString("smcore:accessory.type.str")!!,
            PersistentDataType.STRING,
            Type.name
          )
        }
      }
      if (element is Elements) {
        im.persistentDataContainer.set(
          NamespacedKey.fromString("smcore:item.element.str")!!,
          PersistentDataType.STRING,
          element.toString()
        )
      }
      if (set is SetBonus) {
        im.persistentDataContainer.set(
          NamespacedKey.fromString("smcore:item.set.str")!!,
          PersistentDataType.STRING,
          set.set
        )
      }
      im.persistentDataContainer.set(NamespacedKey.fromString("smcore:item.id.str")!!, PersistentDataType.STRING, id)
      im.persistentDataContainer.set(
        NamespacedKey.fromString("smcore:item.uuid.str")!!,
        PersistentDataType.STRING,
        UUID.randomUUID().toString()
      )
      im.lore(genEquipmentLore(id, rarity, Type, statText, 0, element, 0, set))
      im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES)
      im.addItemFlags(ItemFlag.HIDE_UNBREAKABLE)
      im.addItemFlags(ItemFlag.HIDE_DYE)
      item.itemMeta = im
      return item
    }

    /**
     * Generates Text that displays all stats of Equipment
     * @param stats Stats of the Item
     * @return Text
     */
    fun genStatTexts(stats: HashMap<String, Int>): ArrayList<Component> {
      val statTexts = ArrayList<Component>()
      for (stat in stats) {
        when (stat.key) {
          "damage" -> {
            if (stat.value > 0) {
              statTexts.add(
                createStatText("Damage", stat.value.toString(), NamedTextColor.RED, false).decoration(
                  TextDecoration.ITALIC,
                  false
                )
              )
            }
          }

          "mana" -> {
            if (stat.value > 0) {
              statTexts.add(
                createStatText("Mana", stat.value.toString(), NamedTextColor.AQUA, false).decoration(
                  TextDecoration.ITALIC,
                  false
                )
              )
            }
          }

          "defence" -> {
            if (stat.value > 0) {
              statTexts.add(
                createStatText("Defence", stat.value.toString(), NamedTextColor.GREEN, false).decoration(
                  TextDecoration.ITALIC,
                  false
                )
              )
            }
          }

          "max_health" -> {
            if (stat.value > 0) {
              statTexts.add(
                createStatText("Max Health", stat.value.toString(), NamedTextColor.RED, false).decoration(
                  TextDecoration.ITALIC,
                  false
                )
              )
            }
          }
        }
      }
      return statTexts
    }

    /**
     * Sets the stats of an Item
     * @param stats
     * @param meta Meta of the Item
     * @return Modified Meta
     */
    fun setStats(stats: HashMap<String, Int>, meta: ItemMeta): ItemMeta {
      for (stat in stats) {
        if (stat.value > 0) {
          val key = "smcore:equipment.${stat.key}.int".replace("_", "")
          meta.persistentDataContainer.set(NamespacedKey.fromString(key)!!, PersistentDataType.INTEGER, stat.value)
        }
      }
      return meta
    }

    /**
     * Checks if an Item is an Equipment Item
     * @param item
     * @return if the item is an equipment item
     */
    fun isEquipment(item: ItemStack): Boolean {
      return if (item.hasItemMeta()) {
        item.itemMeta.persistentDataContainer.has(NamespacedKey.fromString("smcore:item.type.str")!!)
      } else {
        false
      }
    }

    /**
     * Generates complete Lore of an Equipment Item
     * @param rarity
     * @param type Type of Equipment
     * @param stats Text of all stats
     * @param xp Amount of xp the item already has
     * @param setCompletion How completed the set of the Item is
     * @param set Set of the Item
     * @return Complete Lore
     */
    fun genEquipmentLore(
      id: String,
      rarity: Rarity,
      type: EquipmentTypes,
      stats: ArrayList<Component>,
      xp: Int,
      element: Elements?,
      setCompletion: Int = 0,
      set: SetBonus?
    ): ArrayList<Component> {
      val lore = ArrayList<Component>()
      lore.addAll(stats)
      lore.add(Component.text("").decoration(TextDecoration.ITALIC, false))
      lore.add(Component.text("Level: ").color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false))
      val level = calcRarityLevel(xp, rarity.multiplier)
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
      val abilities = Abilities()
      if (abilities.getHandler(id) is AbilityHandler) {
        val handler = abilities.getHandler(id)!!
        if (handler.primaryDescription is AbilityDescription) {
          lore.addAll(genAbilityComponent(true, handler.primaryDescription!!, element))
        }
        if (handler.secondaryDescription is AbilityDescription) {
          lore.addAll(genAbilityComponent(false, handler.secondaryDescription!!, element))
        }
      } else {
        val abilityID = if (element is Elements) {
          "${element}_${type}"
        } else {
          "$type"
        }
        val handler = abilities.getHandler(abilityID)
        if (handler is AbilityHandler) {
          if (handler.primaryDescription is AbilityDescription) {
            lore.addAll(genAbilityComponent(true, handler.primaryDescription!!, element).map { it.decoration(TextDecoration.ITALIC, false) })
          }
          if (handler.secondaryDescription is AbilityDescription) {
            lore.addAll(genAbilityComponent(false, handler.secondaryDescription!!, element).map { it.decoration(TextDecoration.ITALIC, false) })
          }
        }
      }
      lore.add(Component.text(""))
      if (set is SetBonus) {
        if (setCompletion == 4) {
          lore.add(
            Component.text("Set Bonus: ${set.bonusName}").color(NamedTextColor.GOLD)
              .append(Component.text(" ($setCompletion/4)").color(NamedTextColor.GREEN))
              .decoration(TextDecoration.ITALIC, false)
          )
        } else {
          lore.add(
            Component.text("Set Bonus: ${set.bonusName}").color(NamedTextColor.GOLD)
              .append(Component.text(" ($setCompletion/4)").color(NamedTextColor.DARK_GRAY))
              .decoration(TextDecoration.ITALIC, false)
          )
        }
        if (set.setLore is ArrayList<Component> && set.setLore!!.size > 0) {
          lore.addAll(set.setLore!!)
        }
        lore.addAll(set.setEffect)
        lore.add(Component.text(""))
      }
      lore.add(
        Component.text("${rarity.name} ${type.name}").color(rarity.color).decoration(TextDecoration.ITALIC, false)
      )
      return lore
    }

    // this is a terrible way of doing this
    // Not that bad actually
    /**
     * Calculates the level of an Item using a multiplier
     * @param xp Xp of the Item
     * @param a Rarity level multiplier
     * @return Level of the Item in Decimal
     */
    fun calcRarityLevel(xp: Int, a: Int): Double {
      // These ARE constants that could have been pre-calculated, however that would look ugly as fuck and it takes
      // literal nanoseconds to calculate this (i hope lmao) (or the compiler optimizes this away, prolly doesnt tho)
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
}
