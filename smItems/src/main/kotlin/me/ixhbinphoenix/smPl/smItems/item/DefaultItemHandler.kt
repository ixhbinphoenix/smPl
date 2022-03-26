package me.ixhbinphoenix.smPl.smItems.item

import me.ixhbinphoenix.smPl.smItems.ItemCategories
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataContainer
import org.bukkit.persistence.PersistentDataType

open class DefaultItemHandler(val item: ItemStack) {
    var category: ItemCategories? = null
    val mat: Material
    val pdc: PersistentDataContainer = item.itemMeta.persistentDataContainer
    init{
        this.category = getItemCategory()
        this.mat = item.type
    }

    fun updateLore() {

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

}