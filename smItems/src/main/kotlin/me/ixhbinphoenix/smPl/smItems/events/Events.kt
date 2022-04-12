package me.ixhbinphoenix.smPl.smItems.events

import me.ixhbinphoenix.smPl.smItems.Main
import me.ixhbinphoenix.smPl.smItems.item.LoreRefresh
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityPickupItemEvent
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
    // More like event.whoAsked
    StatsCalculation(event.whoClicked as Player, true).runTaskLater(plugin, 1)
    if (event.clickedInventory == event.whoClicked.inventory) {
      LoreRefresh(event).runTaskLater(plugin, 1)
    }
  }

  @EventHandler
  fun onPlayerPickUp(event: EntityPickupItemEvent){
    if(event.entity is Player){
      StatsCalculation(event.entity as Player).runTaskLater(plugin, 1)
    }
  }

  @EventHandler
  fun onPlayerDrop(event: PlayerDropItemEvent){
    StatsCalculation(event.player).runTaskLater(plugin, 1)
  }

}