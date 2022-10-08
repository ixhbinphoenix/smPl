package me.ixhbinphoenix.smPl.smCore.entities

import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor

// TODO: Find a better name for this
enum class EntityPosition(val color: TextColor) {
    HOSTILE(NamedTextColor.RED),
    NEUTRAL(NamedTextColor.YELLOW),
    PASSIVE(NamedTextColor.GREEN)
}