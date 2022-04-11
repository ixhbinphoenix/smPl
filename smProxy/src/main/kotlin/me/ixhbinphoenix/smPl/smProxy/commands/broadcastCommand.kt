package me.ixhbinphoenix.smPl.smProxy.commands

import com.velocitypowered.api.command.CommandMeta
import com.velocitypowered.api.command.SimpleCommand
import com.velocitypowered.api.proxy.Player
import me.ixhbinphoenix.smPl.smProxy.getInstance
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration

@Suppress("classname")
class broadcastCommand : SimpleCommand {
  private val instance = getInstance()

  val meta: CommandMeta = instance.server.commandManager.metaBuilder("broadcast")
    .aliases("bc")
    .build()

  override fun execute(invocation: SimpleCommand.Invocation?) {
    if (invocation is SimpleCommand.Invocation) {
      val source = invocation.source()
      val args = invocation.arguments()
      if (args.isEmpty()) {
        source.sendMessage(Component.text("Can't broadcast an empty message!").color(NamedTextColor.RED))
      } else {
        var msg = Component.text("BROADCAST >> ").color(NamedTextColor.YELLOW).decoration(TextDecoration.BOLD, true)
        for (word in args) {
          msg = msg.append(Component.text("$word ").color(NamedTextColor.RED).decoration(TextDecoration.BOLD, false))
        }
        instance.server.sendMessage(msg)
      }
    }
  }

  override fun hasPermission(invocation: SimpleCommand.Invocation?): Boolean {
    return if (invocation is SimpleCommand.Invocation) {
      invocation.source().hasPermission("smproxy.broadcast")
    } else {
      false
    }
  }
}