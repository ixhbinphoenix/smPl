package me.ixhbinphoenix.smPl.smProxy.chat

import com.velocitypowered.api.proxy.Player
import me.ixhbinphoenix.smPl.smProxy.getInstance
import me.ixhbinphoenix.smPl.smProxy.utils.getPlayerRank
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor

class SenateChannel : AbstractChannel() {
  override val channel = ActiveChannel.SENATE
  private val instance = getInstance()

  override fun handleMessage(message: String) {
    if (message.isNotBlank()) {
      val msg = Component.text("[SENATE] ").color(NamedTextColor.YELLOW)
        .append(Component.text("CONSOLE").color(NamedTextColor.DARK_RED))
        .append(Component.text(" >> ").color(NamedTextColor.YELLOW))
        .append(Component.text(message).color(NamedTextColor.WHITE))

      for (receiver in instance.server.allPlayers) {
        if (fulfillsRequirement(receiver)) {
          receiver.sendMessage(msg)
        }
      }

      instance.server.consoleCommandSource.sendMessage(msg)
    } else {
      instance.server.consoleCommandSource.sendMessage(
        Component.text("[SENATE] ").color(NamedTextColor.YELLOW)
          .append(Component.text("Cannot send an empty message!").color(NamedTextColor.RED))
      )
    }
  }

  override fun handleMessage(player: Player, message: String) {
    if (fulfillsRequirement(player)) {
      if (message.isNotBlank()) {
        val msg = Component.text("[SENATE] ").color(NamedTextColor.YELLOW)
          .append(Component.text(player.username).color(getPlayerRank(player).color))
          .append(Component.text(" >> ").color(NamedTextColor.YELLOW))
          .append(Component.text(message).color(NamedTextColor.WHITE))

        for (receiver in instance.server.allPlayers) {
          if (fulfillsRequirement(player)) {
            receiver.sendMessage(msg)
          }
        }
        instance.server.consoleCommandSource.sendMessage(msg)
      } else {
        player.sendMessage(
          Component.text("[SENATE] ").color(NamedTextColor.YELLOW)
            .append(Component.text("Cannot send an empty message!").color(NamedTextColor.RED))
        )
      }
    }
  }

  override fun fulfillsRequirement(player: Player): Boolean {
    return player.hasPermission("smproxy.chat.senate")
  }

}