package me.ixhbinphoenix.smPl.smProxy.commands

import com.velocitypowered.api.command.SimpleCommand
import me.ixhbinphoenix.smPl.smProxy.db.Mute
import me.ixhbinphoenix.smPl.smProxy.db.MuteUtils
import me.ixhbinphoenix.smPl.smProxy.getInstance
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import java.util.UUID
import java.util.concurrent.CompletableFuture
import java.util.function.Supplier

class unmuteCommand : BaseCommand {
  private val instance = getInstance()

  // unmute <username | muteID>
  override fun execute(invocation: SimpleCommand.Invocation?) {
    if (invocation is SimpleCommand.Invocation) {
      val source = invocation.source()
      val args = invocation.arguments()

      if (args.isNotEmpty()) {
        val uuid = instance.uuidCache.getUUIDIfExists(args[0])
        if (uuid is UUID) {
          if (MuteUtils.hasActiveMute(uuid)) {
            MuteUtils.unMute(uuid)
            source.sendMessage(Component.text("Player has been unmuted!").color(NamedTextColor.GREEN))
          } else {
            source.sendMessage(Component.text("Player is not muted!").color(NamedTextColor.RED))
          }
        } else {
          val muteID = args[0].toIntOrNull()
          if (muteID is Int) {
            if (MuteUtils.getMute(muteID) is Mute) {
              MuteUtils.unMute(muteID)
              source.sendMessage(Component.text("Player has been unmuted!").color(NamedTextColor.GREEN))
            } else {
              source.sendMessage(Component.text("Mute with Mute ID $muteID does not exist!"))
            }
          } else {
            source.sendMessage(Component.text("${args[0]} is not a valid player!"))
          }
        }
      }
    }
  }

  override fun hasPermission(invocation: SimpleCommand.Invocation?): Boolean {
    if (invocation is SimpleCommand.Invocation) {
      return invocation.source().hasPermission("smproxy.moderation.mute")
    }
    return false
  }

  override fun suggestAsync(invocation: SimpleCommand.Invocation?): CompletableFuture<MutableList<String>> {
    return CompletableFuture.supplyAsync(Supplier {
      if (invocation is SimpleCommand.Invocation) {
        val args = invocation.arguments()
        if (args.size <= 1) {
          return@Supplier instance.server.allPlayers.map { it.username }.toMutableList()
        }
      }
      return@Supplier mutableListOf<String>()
    })
  }
}