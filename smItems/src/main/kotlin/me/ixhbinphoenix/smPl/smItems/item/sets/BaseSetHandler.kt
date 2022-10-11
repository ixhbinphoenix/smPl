package me.ixhbinphoenix.smPl.smItems.item.sets

import me.ixhbinphoenix.smPl.smItems.item.SetBonus
import org.bukkit.entity.Player

/**
 * Abstract set handler
 */
abstract class BaseSetHandler {
  abstract fun getBonus(): SetBonus
  abstract fun onRecalc(player: Player)
}