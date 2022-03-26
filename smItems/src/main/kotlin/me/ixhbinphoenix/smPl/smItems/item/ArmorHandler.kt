package me.ixhbinphoenix.smPl.smItems.item

import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataContainer
import org.bukkit.persistence.PersistentDataType

class ArmorHandler(item: ItemStack) : DefaultItemHandler(item) {
    var defence: Int
    var maxHealth: Int
    var set: String
    init{
        this.defence = getItemDefence()
        this.maxHealth = getItemMaxHealth()
        this.set = getItemSet()
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

    private fun getItemSet(): String {
        return pdc.getOrDefault(
            NamespacedKey.fromString("smitems:item.set.str")!!,
            PersistentDataType.STRING,
            "NONE"
        )
    }
}