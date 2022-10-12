package me.ixhbinphoenix.smPl.smCore.entities

import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor

// TODO: Find a better name for this
/**
 * Describes the Stance of the Entity towards players
 * @param color The namecolor of the entity
 */
enum class EntityStance(val color: TextColor) {
  HOSTILE(NamedTextColor.RED),
  NEUTRAL(NamedTextColor.YELLOW),
  PASSIVE(NamedTextColor.GREEN)
}