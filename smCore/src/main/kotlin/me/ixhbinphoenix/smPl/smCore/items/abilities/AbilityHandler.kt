package me.ixhbinphoenix.smPl.smCore.items.abilities

import org.bukkit.entity.Player

/**
 * Abstract AbilityHandler
 */
abstract class AbilityHandler {
  abstract val primaryDescription: AbilityDescription?
  abstract val secondaryDescription: AbilityDescription?

  abstract fun onPrimary(player: Player): Boolean
  abstract fun onSecondary(player: Player): Boolean
}