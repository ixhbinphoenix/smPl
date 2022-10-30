package me.ixhbinphoenix.smPl.smCore.items

import me.ixhbinphoenix.smPl.smChat.utils.createStatText
import me.ixhbinphoenix.smPl.smCore.items.sets.SetHelper
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataContainer
import org.bukkit.persistence.PersistentDataType

open class EquipmentHandler(val item: ItemStack, private val player: Player) {
  private val setHelper = SetHelper()

  val rarity: Rarity
  val element: Elements?
  val mat: Material = item.type
  val pdc: PersistentDataContainer = item.itemMeta.persistentDataContainer
  val id: String
  var category: EquipmentCategories?
  var type: EquipmentTypes
  var set: String

  // Stats
  val stats = HashMap<String, Int>()

  var xp: Int
  var level: Double

  init {
    this.rarity = getItemRarity()
    this.element = getItemElement()
    this.id = getItemID()
    this.category = getItemCategory()
    this.type = getItemType()
    this.set = getItemSet()

    val allStats = StatsCalculation.getAllStats()
    for (stat in allStats) {
      this.stats[stat.key] = pdc.getOrDefault(NamespacedKey.fromString(stat.value)!!, PersistentDataType.INTEGER, 0)
    }
    this.xp = getItemXP()
    this.level = ItemUtils.calcRarityLevel(xp, rarity.multiplier)
  }

  fun updateLore() {
    val statTexts = ArrayList<Component>()
    for (stat in stats) {
      if (stat.value > 0) {
        val color = StatsCalculation.statColor(stat.key)
        statTexts.add(
          createStatText(
            StatsCalculation.statNameToDisplayName(stat.key),
            stat.value.toString(),
            color,
            false
          ).decoration(TextDecoration.ITALIC, false)
        )
      }
    }
    val im = item.itemMeta
    im.lore(
      ItemUtils.genEquipmentLore(
        id,
        rarity,
        type,
        statTexts,
        xp,
        element,
        SetHelper.getCompletion(player, set),
        setHelper.setObjects[set]
      )
    )
    item.itemMeta = im
  }

  private fun getItemRarity(): Rarity {
    return Rarity.valueOf(
      pdc.getOrDefault(
        NamespacedKey.fromString("smcore:item.rarity.str")!!,
        PersistentDataType.STRING,
        "COMMON"
      )
    )
  }

  private fun getItemElement(): Elements? {
    return try {
      Elements.valueOf(pdc.get(NamespacedKey.fromString("smcore:item.element.str")!!, PersistentDataType.STRING)!!)
    } catch (e: IllegalArgumentException) {
      null
    } catch (e: NullPointerException) {
      null
    }
  }

  private fun getItemID(): String {
    return pdc.getOrDefault(NamespacedKey.fromString("smcore:item.id.str")!!, PersistentDataType.STRING, "no_id")
  }

  private fun getItemType(): EquipmentTypes {
    when (category) {
      EquipmentCategories.WEAPON -> {
        val stringType = pdc.getOrDefault(
          NamespacedKey.fromString("smcore:weapon.type.str")!!,
          PersistentDataType.STRING,
          ""
        )
        return EquipmentTypes.valueOf(stringType)
      }

      EquipmentCategories.ARMOR -> {
        val stringType = pdc.getOrDefault(
          NamespacedKey.fromString("smcore:armor.type.str")!!,
          PersistentDataType.STRING,
          ""
        )
        return EquipmentTypes.valueOf(stringType)
      }

      else -> {
        throw NotImplementedError("Only WEAPON and ARMOR is Implemented")
      }
    }

  }

  private fun getItemCategory(): EquipmentCategories? {
    val stringType = pdc.getOrDefault(
      NamespacedKey.fromString("smcore:item.type.str")!!,
      PersistentDataType.STRING,
      ""
    )
    return try {
      EquipmentCategories.valueOf(stringType)
    } catch (e: Exception) {
      null
    }
  }

  private fun getItemSet(): String {
    return pdc.getOrDefault(
      NamespacedKey.fromString("smcore:item.set.str")!!,
      PersistentDataType.STRING,
      "NONE"
    )
  }

  private fun getItemXP(): Int {
    return pdc.getOrDefault(
      NamespacedKey.fromString("smcore:item.xp.int")!!,
      PersistentDataType.INTEGER,
      0
    )
  }

  fun setXP(xp: Int) {
    val im = item.itemMeta
    im.persistentDataContainer.set(
      NamespacedKey.fromString("smcore:item.xp.int")!!,
      PersistentDataType.INTEGER,
      xp
    )
    item.itemMeta = im
    this.xp = xp
    this.level = ItemUtils.calcRarityLevel(xp, rarity.multiplier)
    updateLore()
  }
}