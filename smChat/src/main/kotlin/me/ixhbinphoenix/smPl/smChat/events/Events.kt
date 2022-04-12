package me.ixhbinphoenix.smPl.smChat.events

import io.papermc.paper.event.player.AsyncChatEvent
import me.ixhbinphoenix.smPl.smChat.utils.Renderer
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

class Events : Listener {

  @EventHandler
  fun onChat(event: AsyncChatEvent) {
    event.renderer(Renderer())
  }
}