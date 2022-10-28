package me.ixhbinphoenix.smPl.smProxy.chat

import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor

enum class ActiveChannel(val color: TextColor) {
  ALL(NamedTextColor.WHITE),
  PARTY(NamedTextColor.AQUA), // WIP
  GUILD(NamedTextColor.DARK_GREEN), // WIP
  SENATE(NamedTextColor.YELLOW),
  STAFF(NamedTextColor.DARK_PURPLE)
}