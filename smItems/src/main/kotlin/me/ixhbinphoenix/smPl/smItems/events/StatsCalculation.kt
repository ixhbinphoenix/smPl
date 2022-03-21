package me.ixhbinphoenix.smPl.smItems.events

import me.ixhbinphoenix.smPl.smCore.player.PlayerHandler
import me.ixhbinphoenix.smPl.smItems.ItemTypes
import me.ixhbinphoenix.smPl.smItems.item.DefaultItem
import me.ixhbinphoenix.smPl.smItems.item.WeaponItem
import org.bukkit.entity.Player
import org.bukkit.inventory.meta.ItemMeta

class StatsCalculation {
    fun changeStats(oldInventoryItem: ItemMeta, newInventoryItem: ItemMeta, player: Player){
        val playerHandler = PlayerHandler(player)
        val newItem = DefaultItem(newInventoryItem.persistentDataContainer)
        val oldItem = DefaultItem(oldInventoryItem.persistentDataContainer)
        if(newItem.type == oldItem.type){
            when(newItem.type){
                ItemTypes.WEAPON -> {
                    val oldDamage = playerHandler.getDamage()
                    val oldMana = playerHandler.getMana()

                    val oldWeapon = WeaponItem(oldInventoryItem.persistentDataContainer)
                    val newWeapon = WeaponItem(newInventoryItem.persistentDataContainer)
                    val newDamage = oldDamage + (newWeapon.damage - oldWeapon.damage)
                    val newMana = oldMana + (newWeapon.mana - oldWeapon.mana)

                    playerHandler.setDamage(newDamage)
                    playerHandler.setMana(newMana)
                }
            }
        }
    }

    fun changeStats(inventoryItem: ItemMeta, player: Player, isOld: Boolean){
        val playerHandler = PlayerHandler(player)
        val item = DefaultItem(inventoryItem.persistentDataContainer)
        when(item.type){
            ItemTypes.WEAPON -> {
                val oldDamage = playerHandler.getDamage()
                val oldMana = playerHandler.getMana()

                val weapon = WeaponItem(inventoryItem.persistentDataContainer)

                val newDamage: Int
                val newMana: Int

                if(isOld){
                    newDamage = oldDamage - weapon.damage
                    newMana = oldMana - weapon.mana
                }
                else{
                    newDamage = oldDamage + weapon.damage
                    newMana = oldMana + weapon.mana
                }

                playerHandler.setDamage(newDamage)
                playerHandler.setMana(newMana)
            }
        }
    }

}