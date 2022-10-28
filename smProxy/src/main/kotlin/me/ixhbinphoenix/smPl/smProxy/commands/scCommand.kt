package me.ixhbinphoenix.smPl.smProxy.commands

import com.velocitypowered.api.command.SimpleCommand
import com.velocitypowered.api.proxy.Player
import me.ixhbinphoenix.smPl.smProxy.chat.ActiveChannel
import me.ixhbinphoenix.smPl.smProxy.getInstance
import me.ixhbinphoenix.smPl.smProxy.utils.getPlayerRank
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor

@Suppress("classname")
class scCommand : BaseCommand {
  private val handler = getInstance().channelManager.handlers[ActiveChannel.STAFF]!!

  override fun execute(invocation: SimpleCommand.Invocation?) {
    if (invocation is SimpleCommand.Invocation) {
      val source = invocation.source()
      val args = invocation.arguments()
      val msg = args.joinToString(" ")

      if (source is Player) {
        if (handler.fulfillsRequirement(source)) {
          handler.handleMessage(source, msg)
        } else {
          source.sendMessage(Component.text("Insufficient Permissions!").color(NamedTextColor.RED))
        }
      } else {
        handler.handleMessage(msg)
      }
    }
  }

  override fun suggest(invocation: SimpleCommand.Invocation?): MutableList<String> {
    return mutableListOf()
  }

  override fun hasPermission(invocation: SimpleCommand.Invocation?): Boolean {
    return if (invocation is SimpleCommand.Invocation) {
      // Normally, this should be handled by the fulfillsRequirement check, but since staffchat can be used by the console, we can't perform this check
      invocation.source().hasPermission("smproxy.staffchat")
    } else {
      false
    }
  }
}