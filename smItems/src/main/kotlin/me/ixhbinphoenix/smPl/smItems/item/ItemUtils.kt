package me.ixhbinphoenix.smPl.smItems.item

import me.ixhbinphoenix.smPl.smItems.Main
import me.ixhbinphoenix.smPl.smItems.events.StatsCalculation
import me.ixhbinphoenix.smPl.smItems.getInstance
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class ItemUtils {
  private val plugin: Main = getInstance()

  fun giveItem(item: ItemStack, player: Player) {
    player.inventory.addItem(item)
    StatsCalculation(player).runTaskLater(plugin, 2)
  }
}