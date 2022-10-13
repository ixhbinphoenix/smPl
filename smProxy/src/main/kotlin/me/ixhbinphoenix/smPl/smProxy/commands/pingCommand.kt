package me.ixhbinphoenix.smPl.smProxy.commands

import com.velocitypowered.api.command.SimpleCommand
import com.velocitypowered.api.proxy.Player
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor

class pingCommand : BaseCommand {

  override fun execute(invocation: SimpleCommand.Invocation?) {
    if (invocation is SimpleCommand.Invocation) {
      val source = invocation.source()
      if (source is Player) {
        val ping = source.ping
        source.sendMessage(
          Component.text("Your Ping: ").color(NamedTextColor.GOLD)
            .append(Component.text("${ping}ms").color(NamedTextColor.GREEN))
        )
      } else {
        source.sendMessage(
          Component.text("You're the fucking server").color(NamedTextColor.RED)
        )
      }
    }
  }

  override fun hasPermission(invocation: SimpleCommand.Invocation?): Boolean {
    return true
  }
}