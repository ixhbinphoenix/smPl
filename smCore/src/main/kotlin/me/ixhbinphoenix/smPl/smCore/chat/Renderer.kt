package me.ixhbinphoenix.smPl.smCore.chat

import io.papermc.paper.chat.ChatRenderer
import net.kyori.adventure.audience.Audience
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.HoverEvent
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.entity.Player

class Renderer : ChatRenderer {
  override fun render(source: Player, sourceDisplayName: Component, message: Component, viewer: Audience): Component {
    return getPrefix(source, message)
  }
}