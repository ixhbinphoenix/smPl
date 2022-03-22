package me.ixhbinphoenix.smPl.smItems.events

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitRunnable

class IceRunnable(item: ItemStack, slot: Int, player: Player) : BukkitRunnable() {
    val item: ItemStack
    val slot: Int
    val player: Player
    init{
        this.item = item
        this.slot = slot
        this.player = player
    }
    override fun run() {
        val newPlayer = Bukkit.getServer().getPlayer(player.uniqueId)
        val newItem = newPlayer!!.inventory.getItem(slot)
        if(item != newItem){
            val oldItem = item
            if(newItem != null && newItem.hasItemMeta()){
                StatsCalculation().changeStats(newItem.itemMeta, player, false)
            }
            else if(newItem == null && oldItem.hasItemMeta()){
                StatsCalculation().changeStats(oldItem.itemMeta, player, true)
            }
            else if(newItem!!.hasItemMeta() && oldItem.hasItemMeta()){
                StatsCalculation().changeStats(oldItem.itemMeta, newItem.itemMeta, player)
            }
        }
    }
}