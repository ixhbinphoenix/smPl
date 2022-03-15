package me.ixhbinphoenix.smPl.smCore.events

import net.md_5.bungee.api.ChatColor
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

class Events : Listener {
  @EventHandler
  fun onJoin(event: PlayerJoinEvent) {
    event.joinMessage = ChatColor.GREEN.toString() + ">> " + ChatColor.GRAY + event.player.displayName
  }

  @EventHandler
  fun onLeave(event: PlayerQuitEvent) {
    event.quitMessage = ChatColor.RED.toString() + "<< " + ChatColor.GRAY + event.player.displayName
  }
}