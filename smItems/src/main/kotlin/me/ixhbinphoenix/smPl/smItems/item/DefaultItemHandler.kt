package me.ixhbinphoenix.smPl.smItems.item

import me.ixhbinphoenix.smPl.smItems.ItemTypes
import org.bukkit.NamespacedKey
import org.bukkit.persistence.PersistentDataContainer
import org.bukkit.persistence.PersistentDataType

open class DefaultItemHandler(val pdc: PersistentDataContainer) {
    var type: ItemTypes? = null
    init{
        this.type = getItemType()
    }

    private fun getItemType(): ItemTypes? {
        val stringType = pdc.getOrDefault(
            NamespacedKey.fromString("smitems:item.type.str")!!,
            PersistentDataType.STRING,
            ""
        )
        when (stringType) {
            "WEAPON" -> {
                return ItemTypes.WEAPON
            }
            "ARMOR" -> {
                return ItemTypes.ARMOR
            }
            "ACCESSORY" -> {
                return ItemTypes.ACCESSORY
            }
        }
        return null
    }

}