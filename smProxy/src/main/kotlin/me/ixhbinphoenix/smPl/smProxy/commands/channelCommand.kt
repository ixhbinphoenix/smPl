package me.ixhbinphoenix.smPl.smProxy.commands

import com.velocitypowered.api.command.SimpleCommand
import com.velocitypowered.api.proxy.Player
import me.ixhbinphoenix.smPl.smProxy.chat.ActiveChannel
import me.ixhbinphoenix.smPl.smProxy.getInstance
import me.ixhbinphoenix.smPl.smProxy.utils.createPointList
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.event.HoverEvent
import net.kyori.adventure.text.format.NamedTextColor

class channelCommand : BaseCommand {
  private val channelManager = getInstance().channelManager

  override fun execute(invocation: SimpleCommand.Invocation?) {
    if (invocation is SimpleCommand.Invocation) {
      if (invocation.source() is Player) {
        val player = invocation.source() as Player
        val args = invocation.arguments()

        if (args.isEmpty()) {
          val channels = channelManager.getAvailableChannels(player)
          val active = channelManager.getUserChannel(player)

          val channelOptions: ArrayList<Component> = ArrayList(channels.map { channel ->
            val channelName = channel.name.lowercase().replaceFirstChar { ch -> ch.uppercase() }
            if (channel == active) {
              Component.text(channelName).color(channel.color)
                .append(
                  Component.text(" [").color(NamedTextColor.DARK_GRAY)
                    .append(Component.text("✔").color(NamedTextColor.GREEN))
                    .append(Component.text("]").color(NamedTextColor.DARK_GRAY))
                )
            } else {
              Component.text(channelName).color(channel.color)
                .clickEvent(ClickEvent.runCommand("/channel $channelName"))
                .hoverEvent(HoverEvent.showText(Component.text("/channel $channelName").color(NamedTextColor.GOLD)))
            }
          })

          val msg = createPointList(Component.text("Available channels:").color(NamedTextColor.GOLD), channelOptions)

          player.sendMessage(msg)
        } else {
          try {
            val channel = ActiveChannel.valueOf(args[0].uppercase())
            channelManager.setUserChannel(player, channel)
            val channelName = channel.name.lowercase().replaceFirstChar { ch -> ch.uppercase() }
            player.sendMessage(
              Component.text("Set channel to ").color(NamedTextColor.GOLD)
                .append(Component.text(channelName).color(channel.color))
            )
          } catch (e: IllegalArgumentException) {
            player.sendMessage(Component.text("${args[0]} is not a valid channel!").color(NamedTextColor.RED))
          }
        }
      } else {
        invocation.source().sendMessage(Component.text("This command can only be used by players!").color(NamedTextColor.RED))
      }
    }
  }

  override fun suggest(invocation: SimpleCommand.Invocation?): MutableList<String> {
    if (invocation is SimpleCommand.Invocation) {
      if (invocation.source() is Player) {
        val player = invocation.source() as Player
        val channels = channelManager.getAvailableChannels(player)
        val active = channelManager.getUserChannel(player)

        channels.remove(active)
        return channels.map { value -> value.name.lowercase() }.toMutableList()
      }
    }
    return mutableListOf()
  }

  override fun hasPermission(invocation: SimpleCommand.Invocation?): Boolean {
    return true
  }
}