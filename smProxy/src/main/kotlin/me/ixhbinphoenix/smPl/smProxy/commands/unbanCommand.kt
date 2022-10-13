package me.ixhbinphoenix.smPl.smProxy.commands

import com.velocitypowered.api.command.SimpleCommand
import me.ixhbinphoenix.smPl.smProxy.db.Ban
import me.ixhbinphoenix.smPl.smProxy.db.BanUtils
import me.ixhbinphoenix.smPl.smProxy.getInstance
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import java.util.*

class unbanCommand : BaseCommand {
  private val instance = getInstance()

  // /unban <username | banID>
  override fun execute(invocation: SimpleCommand.Invocation?) {
    if (invocation is SimpleCommand.Invocation) {
      val source = invocation.source()
      val args = invocation.arguments()
      if (args.isNotEmpty()) {
        val uuid = instance.uuidCache.getUUIDIfExists(args[0])
        if (uuid is UUID) {
          if (BanUtils.hasActiveBan(uuid)) {
            BanUtils.unBan(uuid)
            source.sendMessage(Component.text("Player has been unbanned!").color(NamedTextColor.GREEN))
          } else {
            source.sendMessage(Component.text("Player is not banned!").color(NamedTextColor.RED))
          }
        } else {
          val banID = args[0].toIntOrNull()
          if (banID is Int) {
            if (BanUtils.getBan(banID) is Ban) {
              BanUtils.unBan(banID)
              source.sendMessage(Component.text("Player has been unbanned!").color(NamedTextColor.GREEN))
            } else {
              source.sendMessage(Component.text("Ban with ID $banID does not exist!").color(NamedTextColor.RED))
            }
          } else {
            source.sendMessage(Component.text("${args[0]} is not a valid Ban ID!").color(NamedTextColor.RED))
          }
        }
      } else {
        source.sendMessage(Component.text("Not enough arguments!").color(NamedTextColor.RED))
      }
    }
  }

  override fun suggest(invocation: SimpleCommand.Invocation?): MutableList<String> {
    if (invocation is SimpleCommand.Invocation) {
      val args = invocation.arguments()
      if (args.size == 1) {
        return instance.server.allPlayers.map { it.username }.toMutableList()
      }
    }
    return mutableListOf()
  }

  override fun hasPermission(invocation: SimpleCommand.Invocation?): Boolean {
    if (invocation is SimpleCommand.Invocation) {
      return invocation.source().hasPermission("smproxy.moderation.unban")
    }
    return false
  }
}