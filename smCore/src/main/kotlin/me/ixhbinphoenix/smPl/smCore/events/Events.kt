package me.ixhbinphoenix.smPl.smCore.events

import io.papermc.paper.event.player.AsyncChatEvent
import me.ixhbinphoenix.smPl.smCore.chat.Renderer
import me.ixhbinphoenix.smPl.smCore.chat.getDisplayName
import me.ixhbinphoenix.smPl.smCore.chat.getPlayerInfo
import me.ixhbinphoenix.smPl.smCore.chat.getRankColor
import me.ixhbinphoenix.smPl.smCore.utils.getPlayerRank
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.HoverEvent
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

class Events : Listener {
  @EventHandler
  fun onJoin(event: PlayerJoinEvent) {
    val rank = getPlayerRank(event.player)
    event.player.playerListName(getDisplayName(event.player))
    event.joinMessage(
      Component.text(">> ").color(NamedTextColor.GREEN)
      .append(Component.text(event.player.name).color(getRankColor(rank)).hoverEvent(HoverEvent.showText(getPlayerInfo(event.player))))
    )
  }

  @EventHandler
  fun onLeave(event: PlayerQuitEvent) {
    val rank = getPlayerRank(event.player)
    event.quitMessage(
      Component.text("<< ").color(NamedTextColor.RED)
        .append(Component.text(event.player.name).color(getRankColor(rank)).hoverEvent(HoverEvent.showText(getPlayerInfo(event.player))))
    )
  }

  @EventHandler
  fun onChat(event: AsyncChatEvent) {
    event.renderer(Renderer())
  }
}