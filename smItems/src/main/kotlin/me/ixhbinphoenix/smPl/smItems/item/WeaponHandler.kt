package me.ixhbinphoenix.smPl.smItems.item

import org.bukkit.NamespacedKey
import org.bukkit.persistence.PersistentDataContainer
import org.bukkit.persistence.PersistentDataType

class WeaponHandler(pdc: PersistentDataContainer) : DefaultItemHandler(pdc) {
    var damage: Int
    var mana: Int
    init {
        this.damage = getItemDamage()
        this.mana = getItemMana()
    }

    private fun getItemDamage(): Int{
        return pdc.getOrDefault(
            NamespacedKey.fromString("smitems:weapon.damage.int")!!,
            PersistentDataType.INTEGER,
            0)
    }

    private fun getItemMana(): Int{
        return pdc.getOrDefault(
            NamespacedKey.fromString("smitems:weapon.mana.int")!!,
            PersistentDataType.INTEGER,
            0
        )
    }

}