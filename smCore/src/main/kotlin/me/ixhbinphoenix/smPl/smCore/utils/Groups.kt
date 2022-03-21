package me.ixhbinphoenix.smPl.smCore.utils

import org.bukkit.entity.Player

fun isPlayerInGroup(player: Player, group: String): Boolean {
  return player.hasPermission("group.$group")
}

fun getPlayerRank(player: Player): String {
  throw NotImplementedError("getPlayerRank is not yet implemented")
}