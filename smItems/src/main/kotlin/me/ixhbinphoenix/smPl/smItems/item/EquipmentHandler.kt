package me.ixhbinphoenix.smPl.smItems.item

import me.ixhbinphoenix.smPl.smChat.utils.createStatText
import me.ixhbinphoenix.smPl.smItems.Elements
import me.ixhbinphoenix.smPl.smItems.ItemCategories
import me.ixhbinphoenix.smPl.smItems.Rarity
import me.ixhbinphoenix.smPl.smItems.Types
import me.ixhbinphoenix.smPl.smItems.events.StatsCalculation
import me.ixhbinphoenix.smPl.smItems.item.sets.SetHelper
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
    private val itemUtils = ItemUtils()

    val rarity: Rarity
    val element: Elements?
    val mat: Material = item.type
    val pdc: PersistentDataContainer = item.itemMeta.persistentDataContainer
    var category: ItemCategories?
    var type: Types
    var set: String

    // Stats
    val stats = HashMap<String, Int>()

    var xp: Int
    var level: Double

    init{
        this.rarity = getItemRarity()
        this.element = getItemElement()
        this.category = getItemCategory()
        this.type = getItemType()
        this.set = getItemSet()

        val allStats = StatsCalculation.getAllStats()
        for (stat in allStats) {
            this.stats[stat.key] = pdc.getOrDefault(NamespacedKey.fromString(stat.value)!!, PersistentDataType.INTEGER, 0)
        }
        this.xp = getItemXP()
        this.level = itemUtils.calcRarityLevel(xp, itemUtils.getRarityMultiplier(rarity))
    }

    fun updateLore() {
        val statTexts = ArrayList<Component>()
        for (stat in stats) {
            if (stat.value > 0) {
                val color = StatsCalculation.statColor(stat.key)
                statTexts.add(createStatText(StatsCalculation.statNameToDisplayName(stat.key), stat.value.toString(), color, false).decoration(TextDecoration.ITALIC, false))
            }
        }
        val im = item.itemMeta
        im.lore(itemUtils.genEquipmentLore(rarity, type, statTexts, xp, SetHelper.getCompletion(player, set), setHelper.setObjects[set]))
        item.itemMeta = im
    }

    private fun getItemRarity(): Rarity {
        return Rarity.valueOf(pdc.getOrDefault(NamespacedKey.fromString("smitems:item.rarity.str")!!, PersistentDataType.STRING, "COMMON"))
    }

    private fun getItemElement(): Elements? {
        return try {
            Elements.valueOf(pdc.get(NamespacedKey.fromString("smitems:item.element.str")!!, PersistentDataType.STRING)!!)
        } catch (e: IllegalArgumentException) {
            null
        } catch (e: NullPointerException) {
            null
        }
    }

    private fun getItemType(): Types {
        when (category) {
            ItemCategories.WEAPON -> {
                val stringType = pdc.getOrDefault(
                    NamespacedKey.fromString("smitems:weapon.type.str")!!,
                    PersistentDataType.STRING,
                    ""
                )
                return Types.valueOf(stringType)
            }
            ItemCategories.ARMOR -> {
                val stringType = pdc.getOrDefault(
                    NamespacedKey.fromString("smitems:armor.type.str")!!,
                    PersistentDataType.STRING,
                    ""
                )
                return Types.valueOf(stringType)
            }
            else -> {
                throw NotImplementedError("Only WEAPON and ARMOR is Implemented")
            }
        }

    }

    private fun getItemCategory(): ItemCategories? {
        val stringType = pdc.getOrDefault(
            NamespacedKey.fromString("smitems:item.type.str")!!,
            PersistentDataType.STRING,
            ""
        )
        when (stringType) {
            "WEAPON" -> {
                return ItemCategories.WEAPON
            }
            "ARMOR" -> {
                return ItemCategories.ARMOR
            }
            "ACCESSORY" -> {
                return ItemCategories.ACCESSORY
            }
        }
        return null
    }

    private fun getItemSet(): String {
        return pdc.getOrDefault(
            NamespacedKey.fromString("smitems:item.set.str")!!,
            PersistentDataType.STRING,
            "NONE"
        )
    }

    private fun getItemXP(): Int {
        return pdc.getOrDefault(
            NamespacedKey.fromString("smitems:item.xp.int")!!,
            PersistentDataType.INTEGER,
            0
        )
    }

    fun setXP(xp: Int) {
        val im = item.itemMeta
        im.persistentDataContainer.set(
            NamespacedKey.fromString("smitems:item.xp.int")!!,
            PersistentDataType.INTEGER,
            xp
        )
        item.itemMeta = im
        this.xp = xp
        this.level = itemUtils.calcRarityLevel(xp, itemUtils.getRarityMultiplier(rarity))
        updateLore()
    }
}