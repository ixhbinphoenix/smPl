package me.ixhbinphoenix.smPl.smEntities

import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor

enum class EntityPosition(val color: TextColor) {
    HOSTILE(NamedTextColor.RED),
    NEUTRAL(NamedTextColor.YELLOW),
    PASSIVE(NamedTextColor.GREEN)
}