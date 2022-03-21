package me.ixhbinphoenix.smPl.smItems.item

import org.bukkit.NamespacedKey
import org.bukkit.persistence.PersistentDataContainer
import org.bukkit.persistence.PersistentDataType

class ArmorItem(persistentDataContainer: PersistentDataContainer) : DefaultItem(persistentDataContainer) {
    var defence: Int
    init{
        this.defence = getDefence(persistentDataContainer)
    }

    private fun getDefence(pdc: PersistentDataContainer): Int{
        return pdc.getOrDefault(
            NamespacedKey.fromString("smitems:armor.defence.int")!!,
            PersistentDataType.INTEGER,
            0)
    }
}