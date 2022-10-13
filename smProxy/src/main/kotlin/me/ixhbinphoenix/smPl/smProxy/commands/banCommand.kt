package me.ixhbinphoenix.smPl.smProxy.commands

import com.velocitypowered.api.command.SimpleCommand
import com.velocitypowered.api.proxy.Player
import me.ixhbinphoenix.smPl.smProxy.db.BanUtils
import me.ixhbinphoenix.smPl.smProxy.getInstance
import me.ixhbinphoenix.smPl.smProxy.utils.TimeUtils
import me.ixhbinphoenix.smPl.smProxy.utils.getPermanentBanMessage
import me.ixhbinphoenix.smPl.smProxy.utils.getTemporaryBanMessage
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import java.time.Instant
import java.util.*

class banCommand : BaseCommand {
  private val instance = getInstance()

  // /ban <player> [<time>] <reason>
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
              id = BanUtils.createBan(uuid, source, false, reason, Instant.now().plusMillis(duration))
              for (player in instance.server.allPlayers) {
                if (player.uniqueId == uuid) {
                  player.disconnect(getTemporaryBanMessage(reason, id, duration))
                }
              }
              source.sendMessage(
                Component.text("Banned ").color(NamedTextColor.GREEN)
                  .append(Component.text(args[0]).color(NamedTextColor.YELLOW))
                  .append(Component.text(" for ").color(NamedTextColor.GREEN))
                  .append(Component.text(args[1]).color(NamedTextColor.YELLOW))
                  .append(Component.text(" because of ").color(NamedTextColor.GREEN))
                  .append(Component.text(reason).color(NamedTextColor.YELLOW))
              )
            } else {
              reason = args.drop(1).joinToString(" ")
              id = BanUtils.createBan(uuid, source, true, reason, null)
              for (player in instance.server.allPlayers) {
                if (player.uniqueId == uuid) {
                  player.disconnect(
                    getPermanentBanMessage(reason, id)
                  )
                }
              }
              source.sendMessage(
                Component.text("Banned ").color(NamedTextColor.GREEN)
                  .append(Component.text(args[0]).color(NamedTextColor.YELLOW))
                  .append(Component.text(" because of ").color(NamedTextColor.GREEN))
                  .append(Component.text(reason).color(NamedTextColor.YELLOW))
              )
            }
          } else {
            source.sendMessage(Component.text("Player does not exist!").color(NamedTextColor.RED))
          }
        } else {
          source.sendMessage(Component.text("Not enough arguments!").color(NamedTextColor.RED))
        }
      } else {
        source.sendMessage(Component.text("Banning from the console is not supported yet!").color(NamedTextColor.RED))
      }
    }
  }

  override fun hasPermission(invocation: SimpleCommand.Invocation?): Boolean {
    if (invocation is SimpleCommand.Invocation) {
      return invocation.source().hasPermission("smproxy.moderation.ban")
    }
    return false
  }

  override fun suggest(invocation: SimpleCommand.Invocation?): MutableList<String> {
    if (invocation is SimpleCommand.Invocation) {
      val args = invocation.arguments()
      if (args.size <= 1) {
        return instance.server.allPlayers.map { it.username }.toMutableList()
      }
    }
    return mutableListOf()
  }
}