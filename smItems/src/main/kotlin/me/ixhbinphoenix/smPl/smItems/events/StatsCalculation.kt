package me.ixhbinphoenix.smPl.smItems.events

import me.ixhbinphoenix.smPl.smCore.player.PlayerHandler
import me.ixhbinphoenix.smPl.smItems.ItemTypes
import me.ixhbinphoenix.smPl.smItems.item.ArmorHandler
import me.ixhbinphoenix.smPl.smItems.item.DefaultItemHandler
import me.ixhbinphoenix.smPl.smItems.item.WeaponHandler
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitRunnable

class StatsCalculation(player: Player) : BukkitRunnable() {
    private val player: Player
    init {
        this.player = player
    }

    override fun run() {
        val inventory = player.inventory
        val handler = PlayerHandler(player)
        var damage = 0
        var mana = 0
        var maxHealth = 0
        var defence = 0
        val statsItems = listOf(
            inventory.itemInMainHand,
            inventory.helmet,
            inventory.chestplate,
            inventory.leggings,
            inventory.boots
        )
        for(item: ItemStack? in statsItems){
            if(item != null && item.hasItemMeta()){
                val pDC = item.itemMeta.persistentDataContainer
                when(DefaultItemHandler(pDC).type){
                    ItemTypes.WEAPON -> {
                        if (item == inventory.itemInMainHand) {
                            val weapon = WeaponHandler(pDC)
                            damage += weapon.damage
                            mana += weapon.mana
                        }
                    }
                    ItemTypes.ARMOR -> {
                        if (
                            item == inventory.helmet ||
                            item == inventory.chestplate ||
                            item == inventory.leggings ||
                            item == inventory.boots
                        ) {
                            val armor = ArmorHandler(pDC)
                            maxHealth += armor.maxHealth
                            defence += armor.defence
                        }
                    }
                    else -> {}
                }
            }
        }
        handler.setDamage(damage)
        handler.setMana(mana)
        handler.setMaxHealth(maxHealth)
        handler.setDefence(defence)
    }

}