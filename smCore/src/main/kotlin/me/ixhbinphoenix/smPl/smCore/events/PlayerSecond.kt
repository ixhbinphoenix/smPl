package me.ixhbinphoenix.smPl.smCore.events

import me.ixhbinphoenix.smPl.smCore.player.PlayerHandler
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.NamespacedKey
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Player
import org.bukkit.persistence.PersistentDataType
import org.bukkit.scheduler.BukkitRunnable
import kotlin.math.roundToInt

/**
 * BukkitRunnable that runs every second for every player
 * @param player
 */
class PlayerSecond(val player: Player) : BukkitRunnable() {
  override fun run() {
    val handler = PlayerHandler(player)

    if (handler.getMana() > handler.getCurrentMana()) {
      var diff = handler.getMana() - handler.getCurrentMana()
      // TODO: Replace this
      // Limits the mana regen to a max of 20/s. This should instead scale to max mana and maybe a mana regen buff/stat?
      if (diff > 20) diff = 20
      handler.setCurrentMana(handler.getCurrentMana() + diff)
    }
    val vanillaMaxHealth = player.getAttribute(Attribute.GENERIC_MAX_HEALTH)?.value?.roundToInt() ?: 20
    var maxHealth = player.persistentDataContainer.getOrDefault(
      NamespacedKey.fromString("smcore:player.maxhealth.int")!!,
      PersistentDataType.INTEGER,
      vanillaMaxHealth)
    if (maxHealth < 20) maxHealth = 20
    player.sendActionBar(
      Component.text("${(player.health * (maxHealth / 20)).roundToInt()}/$maxHealth ♥").color(NamedTextColor.RED)
        .append(Component.text(" ${handler.getCurrentMana()}/${handler.getMana()}").color(NamedTextColor.AQUA)
          .append(Component.text(" ⚡").color(NamedTextColor.GOLD))
        )
        .append(Component.text(" ${handler.getDefence()} ⛨").color(NamedTextColor.GREEN))
    )
  }

}