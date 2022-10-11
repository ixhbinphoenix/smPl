package me.ixhbinphoenix.smPl.smItems.item.abilities

import org.bukkit.entity.Player

/**
 * Abstract AbilityHandler
 */
abstract class AbilityHandler {
  abstract fun onPrimary(player: Player): Boolean
  abstract fun onSecondary(player: Player): Boolean
}