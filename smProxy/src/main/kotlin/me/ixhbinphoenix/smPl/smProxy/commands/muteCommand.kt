package me.ixhbinphoenix.smPl.smProxy.commands

import com.velocitypowered.api.command.SimpleCommand
import com.velocitypowered.api.proxy.Player
import me.ixhbinphoenix.smPl.smProxy.db.MuteUtils
import me.ixhbinphoenix.smPl.smProxy.getInstance
import me.ixhbinphoenix.smPl.smProxy.utils.TimeUtils
import me.ixhbinphoenix.smPl.smProxy.utils.getPermanentMuteMessage
import me.ixhbinphoenix.smPl.smProxy.utils.getTemporaryMuteMessage
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import java.time.Instant
import java.util.UUID
import java.util.concurrent.CompletableFuture
import java.util.function.Supplier

class muteCommand : BaseCommand {
  private val instance = getInstance()

  override fun execute(invocation: SimpleCommand.Invocation?) {
    if (invocation is SimpleCommand.Invocation) {
      val source = invocation.source()
      val args = invocation.arguments()

      if (source is Player) {
        if (args.size >= 2) {
          val uuid = instance.uuidCache.getUUIDIfExists(args[0])
          if (uuid is UUID) {
            val duration = TimeUtils.parseString(args[1])
            val reason: String
            val id: Int
            if (duration is Long) {
              reason = args.drop(2).joinToString(" ")
              id = MuteUtils.createMute(uuid, source, false, reason, Instant.now().plusMillis(duration))
              for (player in instance.server.allPlayers) {
                if (player.uniqueId == uuid) {
                  player.sendMessage(getTemporaryMuteMessage(reason, id, duration))
                }
              }
              source.sendMessage(
                Component.text("Muted ").color(NamedTextColor.GREEN)
                  .append(Component.text(args[0]).color(NamedTextColor.YELLOW))
                  .append(Component.text(" for ").color(NamedTextColor.GREEN))
                  .append(Component.text(args[1]).color(NamedTextColor.YELLOW))
                  .append(Component.text(" because of ").color(NamedTextColor.GREEN))
                  .append(Component.text(reason).color(NamedTextColor.YELLOW))
              )
            } else {
              reason = args.drop(1).joinToString(" ")
              id = MuteUtils.createMute(uuid, source, true, reason, null)
              for (player in instance.server.allPlayers) {
                if (player.uniqueId == uuid) {
                  player.sendMessage(getPermanentMuteMessage(reason, id))
                }
              }
              source.sendMessage(
                Component.text("Muted ").color(NamedTextColor.GREEN)
                  .append(Component.text(args[0]).color(NamedTextColor.YELLOW))
                  .append(Component.text(" because of ").color(NamedTextColor.GREEN))
                  .append(Component.text(reason).color(NamedTextColor.YELLOW))
              )
            }
          }
        } else {
          source.sendMessage(Component.text("Not enough arguments! Usage: /mute <player> [<time>] <reason>"))
        }
      } else {
        source.sendMessage(Component.text("Muting from the console is not supported yet!").color(NamedTextColor.RED))
      }
    }
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

  override fun hasPermission(invocation: SimpleCommand.Invocation?): Boolean {
    if (invocation is SimpleCommand.Invocation) {
      return invocation.source().hasPermission("smproxy.moderation.mute")
    }
    return false
  }
}