package me.ixhbinphoenix.smPl.smProxy.chat

import com.velocitypowered.api.proxy.Player
import me.ixhbinphoenix.smPl.smProxy.getInstance
import me.ixhbinphoenix.smPl.smProxy.utils.getPlayerRank
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor

// TODO: Implement Kyori audiences for this
class StaffChannel : AbstractChannel() {
  private val instance = getInstance()
  override val channel: ActiveChannel = ActiveChannel.STAFF

  override fun handleMessage(message: String) {
    val msg = Component.text("[SC] ").color(NamedTextColor.DARK_PURPLE)
      .append(Component.text("CONSOLE").color(NamedTextColor.DARK_RED))
      .append(Component.text(" >> ").color(NamedTextColor.DARK_PURPLE))
      .append(Component.text(message).color(NamedTextColor.WHITE))
    for (receiver in instance.server.allPlayers) {
      if (fulfillsRequirement(receiver)) {
        receiver.sendMessage(msg)
      }
    }
  }

  override fun handleMessage(player: Player, message: String) {
    // Just in case
    if (fulfillsRequirement(player)) {
      if (message.isNotBlank()) {
        val msg = Component.text("[SC] ").color(NamedTextColor.DARK_PURPLE)
            .append(Component.text(player.username).color(getPlayerRank(player).color))
            .append(Component.text(" >> ").color(NamedTextColor.DARK_PURPLE))
            .append(Component.text(message).color(NamedTextColor.WHITE))
        for (receiver in instance.server.allPlayers) {
          if (fulfillsRequirement(receiver)) {
            receiver.sendMessage(msg)
          }
        }
        instance.server.consoleCommandSource.sendMessage(msg)
      } else {
        player.sendMessage(
          Component.text("[SC] ").color(NamedTextColor.DARK_PURPLE)
            .append(Component.text("Cannot send an empty message!").color(NamedTextColor.RED))
        )
      }
    }
  }

  override fun fulfillsRequirement(player: Player): Boolean {
    return player.hasPermission("smproxy.chat.staff")
  }
}