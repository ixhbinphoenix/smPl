package me.ixhbinphoenix.smPl.smProxy.chat

import com.velocitypowered.api.proxy.Player
import java.util.UUID

class ChannelManager {
  private val cache = HashMap<UUID, ActiveChannel>()
  val handlers: HashMap<ActiveChannel, AbstractChannel> = hashMapOf(
    ActiveChannel.SENATE to SenateChannel(),
    ActiveChannel.STAFF to StaffChannel()
  )

  fun getUserChannel(player: Player): ActiveChannel {
    if (!cache.contains(player.uniqueId)) {
      cache[player.uniqueId] = ActiveChannel.ALL
    }
    return cache[player.uniqueId]!!
  }

  fun getUserChannel(uuid: UUID): ActiveChannel {
    if (!cache.contains(uuid)) {
      cache[uuid] = ActiveChannel.ALL
    }
    return cache[uuid]!!
  }

  fun setUserChannel(player: Player, channel: ActiveChannel) {
    return cache.set(player.uniqueId, channel)
  }

  fun setUserChannel(uuid: UUID, channel: ActiveChannel) {
    return cache.set(uuid, channel)
  }

  fun getAvailableChannels(player: Player): ArrayList<ActiveChannel> {
    val list = arrayListOf(ActiveChannel.ALL)
    for (handler in handlers) {
      if (handler.value.fulfillsRequirement(player)) {
        list.add(handler.value.channel)
      }
    }
    return list
  }
}