package me.ixhbinphoenix.smPl.smItems.events

import me.ixhbinphoenix.smPl.smCore.player.PlayerHandler
import me.ixhbinphoenix.smPl.smItems.ItemTypes
import me.ixhbinphoenix.smPl.smItems.item.DefaultItem
import me.ixhbinphoenix.smPl.smItems.item.WeaponItem
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
                when(DefaultItem(pDC).type){
                    ItemTypes.WEAPON -> {
                        val weapon = WeaponItem(pDC)
                        damage += weapon.damage
                        mana += weapon.mana
                    }
                    else -> {}
                }
            }
        }
        handler.setDamage(damage)
        handler.setMana(mana)
    }

}