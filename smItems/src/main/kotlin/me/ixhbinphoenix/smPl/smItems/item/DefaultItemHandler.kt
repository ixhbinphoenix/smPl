package me.ixhbinphoenix.smPl.smItems.item

import me.ixhbinphoenix.smPl.smCore.chat.createStatText
import me.ixhbinphoenix.smPl.smItems.ItemCategories
import me.ixhbinphoenix.smPl.smItems.Rarity
import me.ixhbinphoenix.smPl.smItems.Types
import me.ixhbinphoenix.smPl.smItems.item.sets.SetHelper
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.entity.Item
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataContainer
import org.bukkit.persistence.PersistentDataType

open class DefaultItemHandler(val item: ItemStack, val player: Player) {
    var category: ItemCategories? = null
    val mat: Material
    val pdc: PersistentDataContainer = item.itemMeta.persistentDataContainer
    val setHelper = SetHelper()
    var set: String
    private val ItemUtils = ItemUtils()
    init{
        this.category = getItemCategory()
        this.mat = item.type
        this.set = getItemSet()
    }

    fun updateLore() {
        val rarity = Rarity.valueOf(pdc.getOrDefault(NamespacedKey.fromString("smitems:item.rarity.str")!!, PersistentDataType.STRING, "COMMON"))
        val type = when (category) {
            ItemCategories.WEAPON -> {
                Types.valueOf(pdc.getOrDefault(NamespacedKey.fromString("smitems:weapon.type.str")!!, PersistentDataType.STRING, "SWORD"))
            }
            ItemCategories.ARMOR -> {
                Types.valueOf(pdc.getOrDefault(NamespacedKey.fromString("smitems:armor.type.str")!!, PersistentDataType.STRING, "HELMET"))
            }
            else -> {
                throw NotImplementedError("Only WEAPON and ARMOR is Implemented")
            }
        }
        val stats = HashMap<String, Int>()
        stats["Damage"] = pdc.getOrDefault(NamespacedKey.fromString("smitems:weapon.damage.int")!!, PersistentDataType.INTEGER, 0)
        stats["Mana"] = pdc.getOrDefault(NamespacedKey.fromString("smitems:weapon.mana.int")!!, PersistentDataType.INTEGER, 0)
        stats["Defence"] = pdc.getOrDefault(NamespacedKey.fromString("smitems:armor.defence.int")!!, PersistentDataType.INTEGER, 0)
        stats["Max Health"] = pdc.getOrDefault(NamespacedKey.fromString("smitems:armor.maxhealth.int")!!, PersistentDataType.INTEGER, 0)
        val statTexts = ArrayList<Component>()
        for (stat in stats) {
            if (stat.value > 0) {
                val color: TextColor = when (stat.key) {
                    "Damage" -> {
                        NamedTextColor.RED
                    }
                    "Mana" -> {
                        NamedTextColor.AQUA
                    }
                    "Defence" -> {
                        NamedTextColor.GREEN
                    }
                    "Max Health" -> {
                        NamedTextColor.RED
                    }
                    else -> {
                        NamedTextColor.YELLOW
                    }
                }
                statTexts.add(createStatText(stat.key, stat.value.toString(), color, false).decoration(TextDecoration.ITALIC, false))
            }
        }
        val im = item.itemMeta
        im.lore(ItemUtils.genLore(rarity, type, statTexts, SetHelper.getCompletion(player, set), setHelper.setObjects[set]))
        item.itemMeta = im
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

}