package me.ixhbinphoenix.smPl.smItems.events

import me.ixhbinphoenix.smPl.smItems.Main
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityPickupItemEvent
import org.bukkit.event.inventory.InventoryAction
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.event.player.PlayerItemHeldEvent

class Events : Listener {

  @EventHandler
  fun onPlayerEquip(event: PlayerItemHeldEvent) {
    val oldItem = event.player.inventory.getItem(event.previousSlot)
    val newItem = event.player.inventory.getItem(event.newSlot)
    @Suppress("SENSELESS_COMPARISON")
    if(oldItem == null || !oldItem.hasItemMeta()){
      if(newItem != null && newItem.hasItemMeta()){
        StatsCalculation().changeStats(newItem.itemMeta, event.player, false)
      }
    }
    else if(newItem == null || !newItem.hasItemMeta()){
      if(oldItem != null && oldItem.hasItemMeta()){
        StatsCalculation().changeStats(oldItem.itemMeta, event.player, true)
      }
    }
    else if (newItem != null && oldItem != null) {
      if(newItem.hasItemMeta() && oldItem.hasItemMeta()){
        StatsCalculation().changeStats(oldItem.itemMeta, newItem.itemMeta, event.player)
      }
    }
  }

  @EventHandler
  fun onInventoryClick(event: InventoryClickEvent){
    if(event.inventory.viewers.size >= 2 ||
      event.action == InventoryAction.DROP_ALL_SLOT ||
      event.action == InventoryAction.DROP_ONE_SLOT) return
    if(event.action == InventoryAction.DROP_ALL_CURSOR ||
      event.action == InventoryAction.DROP_ONE_CURSOR
      && event.cursor != null && event.cursor!!.hasItemMeta()){
      StatsCalculation().changeStats(event.cursor!!.itemMeta, event.whoClicked as Player, false)
      return
    }
    val player = event.whoClicked as Player
    val slot = event.slot
    val item = event.currentItem
    if(slot == player.inventory.heldItemSlot && item != null){
      IceRunnable(item, slot, player).runTaskLater(Bukkit.getPluginManager().getPlugin("smItems") as Main, 1)
    }
  }

  @EventHandler
  fun onPlayerPickUp(event: EntityPickupItemEvent){
    if(event.entity is Player){
      val player = event.entity as Player
      val item = player.inventory.itemInMainHand
      IceRunnable(item, player.inventory.heldItemSlot, player).runTaskLater(Bukkit.getPluginManager().getPlugin("smItems") as Main, 1)
    }
  }

  @EventHandler
  fun onPlayerDrop(event: PlayerDropItemEvent){
    if(event.itemDrop.itemStack.hasItemMeta() && event.player.itemOnCursor != event.itemDrop.itemStack){
      StatsCalculation().changeStats(event.itemDrop.itemStack.itemMeta, event.player, true)
    }
  }

}