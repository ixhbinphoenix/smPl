package me.ixhbinphoenix.smPl.smProxy.events

import com.velocitypowered.api.event.PostOrder
import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.player.PlayerChatEvent
import com.velocitypowered.api.event.player.ServerPreConnectEvent
import me.ixhbinphoenix.smPl.smProxy.chat.ActiveChannel
import me.ixhbinphoenix.smPl.smProxy.db.BanUtils
import me.ixhbinphoenix.smPl.smProxy.db.MuteUtils
import me.ixhbinphoenix.smPl.smProxy.getInstance
import me.ixhbinphoenix.smPl.smProxy.utils.*
import java.time.Instant

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
        event.player.disconnect(
          getTemporaryBanMessage(
            ban.reason ?: "No Reason specified",
            ban.id.value,
            (ban.expiry?.toEpochMilli() ?: Instant.now().toEpochMilli()) - Instant.now().toEpochMilli()
          )
        )
      }
    }
    if (event.originalServer.serverInfo.name == "build") {
      if (getPlayerRank(event.player).rank != GroupRanks.TEAM) {
        event.result = ServerPreConnectEvent.ServerResult.denied()
      }
    }
  }

  @Subscribe(order = PostOrder.FIRST)
  fun onPlayerChat(event: PlayerChatEvent) {
    if (MuteUtils.hasActiveMute(event.player.uniqueId)) {
      val mute = MuteUtils.getSeverestMute(MuteUtils.getActiveMutes(event.player.uniqueId))
      if (mute.permanent) {
        event.player.sendMessage(getPermanentMuteMessage(mute.reason ?: "No Reason specified", mute.id.value))
        event.result = PlayerChatEvent.ChatResult.denied()
      } else {
        event.player.sendMessage(
          getTemporaryMuteMessage(
            mute.reason ?: "No Reason specified",
            mute.id.value,
            (mute.expiry?.toEpochMilli() ?: Instant.now().toEpochMilli()) - Instant.now().toEpochMilli()
          )
        )
        event.result = PlayerChatEvent.ChatResult.denied()
      }
    } else {
      val userChannel = instance.channelManager.getUserChannel(event.player)
      if (userChannel != ActiveChannel.ALL) {
        event.result = PlayerChatEvent.ChatResult.denied()
        val handler = instance.channelManager.handlers[userChannel]!!
        handler.handleMessage(event.player, event.message)
      }
    }
  }
}