package me.ixhbinphoenix.smPl.smProxy.events

import com.velocitypowered.api.event.PostOrder
import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.player.ServerPreConnectEvent
import me.ixhbinphoenix.smPl.smProxy.db.BanUtils
import me.ixhbinphoenix.smPl.smProxy.db.Bans
import me.ixhbinphoenix.smPl.smProxy.getInstance
import me.ixhbinphoenix.smPl.smProxy.utils.GroupRanks
import me.ixhbinphoenix.smPl.smProxy.utils.getPermanentBanMessage
import me.ixhbinphoenix.smPl.smProxy.utils.getPlayerRank
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor

class Events {
  private val instance = getInstance()

  @Subscribe(order = PostOrder.FIRST)
  fun onPlayerLogin(event: ServerPreConnectEvent) {
    instance.uuidCache.addPlayer(event.player)
    if (BanUtils.hasActiveBan(event.player.uniqueId)) {
      val ban = BanUtils.getSeverestBan(BanUtils.getActiveBans(event.player.uniqueId))
      if (ban.permanent) {
        event.player.disconnect(getPermanentBanMessage(ban.reason ?: "No Reason specified", ban.id.value))
      } else {
        event.player.disconnect(Component.text("Ban Messages for temporary bans have not yet been implemented!").color(NamedTextColor.RED))
      }
    }
    if (event.originalServer.serverInfo.name == "build") {
      if (getPlayerRank(event.player).rank != GroupRanks.TEAM) {
        event.result = ServerPreConnectEvent.ServerResult.denied()
      }
    }
  }
}