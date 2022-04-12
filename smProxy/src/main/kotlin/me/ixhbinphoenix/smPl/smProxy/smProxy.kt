package me.ixhbinphoenix.smPl.smProxy

import com.google.inject.Inject
import com.velocitypowered.api.event.PostOrder
import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent
import com.velocitypowered.api.plugin.Plugin
import com.velocitypowered.api.proxy.ProxyServer
import me.ixhbinphoenix.smPl.smProxy.commands.broadcastCommand
import me.ixhbinphoenix.smPl.smProxy.commands.pingCommand
import me.ixhbinphoenix.smPl.smProxy.commands.scCommand
import me.ixhbinphoenix.smPl.smProxy.events.Events
import org.slf4j.Logger

@Suppress("unused", "ClassName")
@Plugin(id = "smproxy", name = "smProxy", version = "0.0.2", url = "https://github.com/ixhbinphoenix/smPl", description = "Stahlmetall plugins", authors = ["ixhbinphoenix", "renkertm"])
class smProxy @Inject constructor(val server: ProxyServer, val logger: Logger) {

  init {
    instance = this
  }

  @Subscribe(order = PostOrder.FIRST)
  fun onInit(event: ProxyInitializeEvent) {
    server.eventManager.register(this, Events())

    server.commandManager.register(scCommand().meta, scCommand())
    server.commandManager.register(broadcastCommand().meta, broadcastCommand())
    server.commandManager.register(pingCommand().meta, pingCommand())

    logger.info("smProxy enabled")
  }

  companion object {
    lateinit var instance: smProxy
  }
}

fun getInstance(): smProxy {
  return smProxy.instance
}