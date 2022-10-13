package me.ixhbinphoenix.smPl.smProxy.utils

import com.velocitypowered.api.proxy.Player
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor

// GROUPS
// - Team
//  - Admin
//  - Moderator
//  - Builder
// - Senate
//  - Senator
// - Player
//  - Trusted
//  - Default

enum class GroupRanks {
  TEAM,
  SENATE,
  PLAYER
}

enum class Rank(val rank: GroupRanks, val color: TextColor) {
  ADMIN(GroupRanks.TEAM, TextColor.fromCSSHexString("#21abcd")!!),
  MODERATOR(GroupRanks.TEAM, NamedTextColor.RED),
  BUILDER(GroupRanks.TEAM, NamedTextColor.GREEN),
  SENATOR(GroupRanks.SENATE, TextColor.fromCSSHexString("#efcc00")!!),
  TRUSTED(GroupRanks.PLAYER, NamedTextColor.LIGHT_PURPLE),
  PLAYER(GroupRanks.PLAYER, NamedTextColor.GRAY)
}

fun isPlayerInRank(player: Player, group: String): Boolean {
  return player.hasPermission("group.$group")
}

fun getPlayerRank(player: Player): Rank {
  for (rank in Rank.values()) {
    if (player.hasPermission("group.${rank.name.lowercase()}")) {
      return rank
    }
  }
  return Rank.PLAYER
}