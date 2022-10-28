package me.ixhbinphoenix.smPl.smProxy.chat

import com.velocitypowered.api.proxy.Player

abstract class AbstractChannel {
  abstract val channel: ActiveChannel

  abstract fun handleMessage(message: String)
  abstract fun handleMessage(player: Player, message: String)
  abstract fun fulfillsRequirement(player: Player): Boolean
}