package me.ixhbinphoenix.smPl.smCore.events

import io.papermc.paper.event.player.AsyncChatEvent
import me.ixhbinphoenix.smPl.smCore.chat.Renderer
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

class Events : Listener {
  @EventHandler
  fun onJoin(event: PlayerJoinEvent) {
    event.joinMessage(Component.text(">> ").color(NamedTextColor.GREEN).append(event.player.displayName().color(NamedTextColor.GRAY)))
  }

  @EventHandler
  fun onLeave(event: PlayerQuitEvent) {
    event.quitMessage(Component.text("<< ").color(NamedTextColor.RED).append(event.player.displayName().color(NamedTextColor.GRAY)))
  }

  @EventHandler
  fun onChat(event: AsyncChatEvent) {
    event.renderer(Renderer())
  }
}