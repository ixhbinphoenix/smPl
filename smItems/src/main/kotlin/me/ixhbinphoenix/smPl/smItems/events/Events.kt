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
  private val plugin = Bukkit.getPluginManager().getPlugin("smItems") as Main

  @EventHandler
  fun onPlayerEquip(event: PlayerItemHeldEvent) {
    StatsCalculation(event.player).runTaskLater(plugin, 2)
  }

  @EventHandler
  fun onInventoryClick(event: InventoryClickEvent){
    StatsCalculation(event.whoClicked as Player).runTaskLater(plugin, 1)
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
    StatsCalculation(event.player).runTaskLater(plugin, 1)
  }

}