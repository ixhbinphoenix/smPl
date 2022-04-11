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

enum class Rank(val rank: GroupRanks) {
  ADMIN(GroupRanks.TEAM),
  MODERATOR(GroupRanks.TEAM),
  BUILDER(GroupRanks.TEAM),
  SENATOR(GroupRanks.SENATE),
  TRUSTED(GroupRanks.PLAYER),
  PLAYER(GroupRanks.PLAYER)
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

fun getRankColor(rank: Rank): TextColor {
  return when(rank) {
    Rank.ADMIN -> TextColor.fromCSSHexString("#21abcd")!!
    Rank.MODERATOR -> NamedTextColor.RED
    Rank.BUILDER -> NamedTextColor.GREEN
    Rank.SENATOR -> TextColor.fromCSSHexString("#efcc00")!!
    Rank.TRUSTED -> NamedTextColor.LIGHT_PURPLE
    Rank.PLAYER -> NamedTextColor.GRAY
  }
}