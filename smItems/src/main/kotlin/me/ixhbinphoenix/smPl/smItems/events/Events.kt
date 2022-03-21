package me.ixhbinphoenix.smPl.smItems.events

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.event.player.PlayerItemHeldEvent

class Events : Listener {
  @EventHandler
  fun onPlayerEquip(event: PlayerItemHeldEvent) {
    val oldItem = event.player.inventory.getItem(event.previousSlot)
    val newItem = event.player.inventory.getItem(event.newSlot)
    if(oldItem == null && newItem != null && newItem.hasItemMeta()){
      StatsCalculation().changeStats(newItem.itemMeta, event.player, false)
    }
    else if(newItem == null && oldItem!!.hasItemMeta()){
      StatsCalculation().changeStats(oldItem.itemMeta, event.player, true)
    }
    else if(newItem!!.hasItemMeta() && oldItem!!.hasItemMeta()){
      StatsCalculation().changeStats(oldItem.itemMeta, newItem.itemMeta, event.player)
    }
  }

  @EventHandler
  fun onPlayerDrop(event: PlayerDropItemEvent){
    event.isCancelled = true
  }

}