package me.ixhbinphoenix.smPl.smItems.item

import org.bukkit.NamespacedKey
import org.bukkit.persistence.PersistentDataContainer
import org.bukkit.persistence.PersistentDataType

class WeaponItem(persistentDataContainer: PersistentDataContainer) : DefaultItem(persistentDataContainer) {
    var damage: Int
    var mana: Int
    init {
        this.damage = getDamage(persistentDataContainer)
        this.mana = getMana(persistentDataContainer)
    }

    private fun getDamage(pdc: PersistentDataContainer): Int{
        return pdc.getOrDefault(
            NamespacedKey.fromString("smitems:weapon.damage.int")!!,
            PersistentDataType.INTEGER,
            0)
    }

    private fun getMana(pdc: PersistentDataContainer): Int{
        return pdc.getOrDefault(
            NamespacedKey.fromString("smitems:weapon.mana.int")!!,
            PersistentDataType.INTEGER,
            0
        )
    }

}