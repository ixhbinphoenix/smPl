package me.ixhbinphoenix.smPl.smItems.item

import me.ixhbinphoenix.smPl.smItems.ItemTypes
import org.bukkit.NamespacedKey
import org.bukkit.persistence.PersistentDataContainer
import org.bukkit.persistence.PersistentDataType

open class DefaultItem(persistentDataContainer: PersistentDataContainer) {
    var type: ItemTypes? = null
    init{
        this.type = getType(persistentDataContainer)
    }

    private fun getType(pdc: PersistentDataContainer): ItemTypes? {
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