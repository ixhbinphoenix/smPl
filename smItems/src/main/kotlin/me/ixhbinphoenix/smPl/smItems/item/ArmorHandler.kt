package me.ixhbinphoenix.smPl.smItems.item

import org.bukkit.NamespacedKey
import org.bukkit.persistence.PersistentDataContainer
import org.bukkit.persistence.PersistentDataType

class ArmorHandler(pdc: PersistentDataContainer) : DefaultItemHandler(pdc) {
    var defence: Int
    var maxHealth: Int
    init{
        this.defence = getItemDefence()
        this.maxHealth = getItemMaxHealth()
    }

    private fun getItemDefence(): Int{
        return pdc.getOrDefault(
            NamespacedKey.fromString("smitems:armor.defence.int")!!,
            PersistentDataType.INTEGER,
            0)
    }

    private fun getItemMaxHealth(): Int {
        return pdc.getOrDefault(
            NamespacedKey.fromString("smitems:armor.maxhealth.int")!!,
            PersistentDataType.INTEGER,
            0
        )
    }
}