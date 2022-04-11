package me.ixhbinphoenix.smPl.smProxy.events

import com.velocitypowered.api.event.PostOrder
import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.player.KickedFromServerEvent
import com.velocitypowered.api.event.player.PlayerChatEvent
import com.velocitypowered.api.event.player.ServerPreConnectEvent
import me.ixhbinphoenix.smPl.smProxy.getInstance
import me.ixhbinphoenix.smPl.smProxy.utils.GroupRanks
import me.ixhbinphoenix.smPl.smProxy.utils.getPlayerRank
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor

class Events {
  private val instance = getInstance()
  private val server = instance.server
  private val logger = instance.logger

  @Subscribe(order = PostOrder.EARLY)
  fun onPlayerLogin(event: ServerPreConnectEvent) {
    if (event.originalServer.serverInfo.name == "build") {
      if (getPlayerRank(event.player).rank != GroupRanks.TEAM) {
        event.result = ServerPreConnectEvent.ServerResult.denied()
      }
    }
  }
}