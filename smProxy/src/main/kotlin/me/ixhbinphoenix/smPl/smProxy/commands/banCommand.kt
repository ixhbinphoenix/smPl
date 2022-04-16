package me.ixhbinphoenix.smPl.smProxy.commands

import com.velocitypowered.api.command.SimpleCommand
import com.velocitypowered.api.proxy.Player
import me.ixhbinphoenix.smPl.smProxy.db.BanUtils
import me.ixhbinphoenix.smPl.smProxy.getInstance
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import java.util.*

class banCommand : BaseCommand {
    private val instance = getInstance()

    // Only for permanent bans!
    // /ban <player> <reason>
    override fun execute(invocation: SimpleCommand.Invocation?) {
        if (invocation is SimpleCommand.Invocation) {
            val source = invocation.source()
            val args = invocation.arguments()
            if (source is Player) {
                if (args.size >= 2) {
                    val uuid = instance.uuidCache.getUUIDIfExists(args[0])
                    if (uuid is UUID) {
                        val reason = args.drop(1).joinToString(" ")
                        val id = BanUtils.createBan(uuid, source, true, reason, null)
                        for (player in instance.server.allPlayers) {
                            if (player.uniqueId == uuid) {
                                player.disconnect(
                                    Component.text("You have been permanently banned from the network!\n").color(NamedTextColor.RED)
                                        .append(Component.text("Reason: ").color(NamedTextColor.RED)
                                            .append(Component.text(reason).color(NamedTextColor.YELLOW))
                                        )
                                        .append(Component.text("\n"))
                                        .append(Component.text("Ban ID: ").color(NamedTextColor.RED)
                                            .append(Component.text("#$id").color(NamedTextColor.YELLOW))
                                        )
                                )
                            }
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
            if (args.size == 1) {
                return instance.server.allPlayers.map { it.username }.toMutableList()
            }
        }
        return mutableListOf()
    }
}