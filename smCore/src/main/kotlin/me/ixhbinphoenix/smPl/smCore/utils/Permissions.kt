package me.ixhbinphoenix.smPl.smCore.utils

import org.bukkit.entity.Player

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