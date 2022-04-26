package me.ixhbinphoenix.smPl.smCore.events

import me.ixhbinphoenix.smPl.smCore.getInstance
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import java.util.UUID

class Events : Listener {
  private val runnableMap = HashMap<UUID, PlayerSecond>()
  private val instance = getInstance()

  @EventHandler
  fun onPlayerJoin(event: PlayerJoinEvent) {
    runnableMap[event.player.uniqueId] = PlayerSecond(event.player)
    runnableMap[event.player.uniqueId]!!.runTaskTimerAsynchronously(instance, 0, 20)
  }

  @EventHandler
  fun onPlayerLeave(event: PlayerQuitEvent) {
    runnableMap[event.player.uniqueId]!!.cancel()
  }
}