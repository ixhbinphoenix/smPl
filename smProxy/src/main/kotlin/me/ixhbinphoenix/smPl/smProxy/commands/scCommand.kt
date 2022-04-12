package me.ixhbinphoenix.smPl.smProxy.commands

import com.velocitypowered.api.command.CommandMeta
import com.velocitypowered.api.command.SimpleCommand
import com.velocitypowered.api.proxy.Player
import me.ixhbinphoenix.smPl.smProxy.getInstance
import me.ixhbinphoenix.smPl.smProxy.utils.getPlayerRank
import me.ixhbinphoenix.smPl.smProxy.utils.getRankColor
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor

@Suppress("classname")
class scCommand : SimpleCommand {
  private val instance = getInstance()

  val meta: CommandMeta = instance.server.commandManager.metaBuilder("sc").build()

  override fun execute(invocation: SimpleCommand.Invocation?) {
    if (invocation is SimpleCommand.Invocation) {
      val source = invocation.source()
      val args = invocation.arguments()
      if (args.isEmpty()) {
        source.sendMessage(
          Component.text("[SC] ").color(NamedTextColor.DARK_PURPLE)
            .append(Component.text("Can't send an empty message!").color(NamedTextColor.RED))
        )
      } else {
        if (source is Player) {
          var msg = Component.text("[SC] ").color(NamedTextColor.DARK_PURPLE)
            .append(Component.text(source.username).color(getRankColor(getPlayerRank(source))))
            .append(Component.text(" >> ").color(NamedTextColor.DARK_PURPLE))

          for (word in args) {
            msg = msg.append(Component.text("$word ").color(NamedTextColor.LIGHT_PURPLE))
          }
          for (player in instance.server.allPlayers) {
            if (player.hasPermission("smproxy.staffchat")) {
              player.sendMessage(msg)
            }
          }
          instance.server.consoleCommandSource.sendMessage(msg)
        } else {
          var msg = Component.text("[SC] ").color(NamedTextColor.DARK_PURPLE)
            .append(Component.text("CONSOLE").color(NamedTextColor.DARK_RED))
            .append(Component.text(" >> ").color(NamedTextColor.DARK_PURPLE))
          for (word in args) {
            msg = msg.append(Component.text("$word ").color(NamedTextColor.LIGHT_PURPLE))
          }
          for (player in instance.server.allPlayers) {
            if (player.hasPermission("smproxy.staffchat")) {
              player.sendMessage(msg)
            }
          }
          instance.server.consoleCommandSource.sendMessage(msg)
        }
      }
    }
  }

  override fun suggest(invocation: SimpleCommand.Invocation?): MutableList<String> {
    return mutableListOf()
  }

  override fun hasPermission(invocation: SimpleCommand.Invocation?): Boolean {
    return if (invocation is SimpleCommand.Invocation) {
      invocation.source().hasPermission("smproxy.staffchat")
    } else {
      false
    }
  }
}