package me.ixhbinphoenix.smPl.smItems.item.sets

import me.ixhbinphoenix.smPl.smCore.player.PlayerHandler
import org.bukkit.entity.Player

class ProgrammerSet : BaseSetHandler() {
  override fun onRecalc(player: Player) {
    val handler = PlayerHandler(player)
    val mana = handler.getMana() + 10000
    handler.setMana(mana)
  }
}