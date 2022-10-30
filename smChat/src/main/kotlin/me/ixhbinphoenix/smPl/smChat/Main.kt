package me.ixhbinphoenix.smPl.smChat

import me.ixhbinphoenix.smPl.smChat.commands.BaseCommand
import me.ixhbinphoenix.smPl.smChat.commands.TogglePingCommand
import me.ixhbinphoenix.smPl.smChat.events.Events
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.plugin.java.JavaPlugin

@Suppress("unused")
class Main : JavaPlugin() {
  override fun onEnable() {
    val commands: HashMap<String, BaseCommand> = hashMapOf(
      "toggleping" to TogglePingCommand()
    )
    for (cmd in commands) {
      getCommand(cmd.key)?.setExecutor(cmd.value)
      getCommand(cmd.key)?.tabCompleter = cmd.value
    }

    server.pluginManager.registerEvents(Events(), this)
    server.consoleSender.sendMessage(Component.text("smChat enabled").color(NamedTextColor.GREEN))
  }
}