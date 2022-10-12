package me.ixhbinphoenix.smPl.smCore.items.sets

import me.ixhbinphoenix.smPl.smCore.items.SetBonus
import org.bukkit.entity.Player

/**
 * Abstract set handler
 */
abstract class BaseSetHandler {
  abstract fun getBonus(): SetBonus
  abstract fun onRecalc(player: Player)
}