package me.ixhbinphoenix.smPl.smCore.items.sets

import me.ixhbinphoenix.smPl.smCore.player.PlayerHandler
import me.ixhbinphoenix.smPl.smCore.items.SetBonus
import me.ixhbinphoenix.smPl.smCore.items.sets.BaseSetHandler
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.entity.Player

class ProgrammerSet : BaseSetHandler() {

  override fun getBonus(): SetBonus {
    val set = SetBonus
    set.bonusName = "Programmer"
    set.set = "PROGRAMMER"

    val setLore = ArrayList<Component>()
    setLore.add(Component.text("The ultimate outfit for a Programmer").color(NamedTextColor.DARK_GRAY))
    set.setLore = setLore

    val effect = ArrayList<Component>()
    effect.add(Component.text("Gain 10,000 Mana").color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false))
    set.setEffect = effect
    return set
  }

  override fun onRecalc(player: Player) {
    val handler = PlayerHandler(player)
    val mana = handler.getMana() + 10000
    handler.setMana(mana)
  }
}