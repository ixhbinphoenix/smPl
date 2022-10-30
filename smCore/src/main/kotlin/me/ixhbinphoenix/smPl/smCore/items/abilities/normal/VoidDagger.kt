package me.ixhbinphoenix.smPl.smCore.items.abilities.normal

import me.ixhbinphoenix.smPl.smCore.entities.EntityHandler
import me.ixhbinphoenix.smPl.smCore.items.Elements
import me.ixhbinphoenix.smPl.smCore.items.abilities.AbilityDescription
import me.ixhbinphoenix.smPl.smCore.items.abilities.AbilityHandler
import me.ixhbinphoenix.smPl.smCore.player.PlayerHandler
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.FluidCollisionMode
import org.bukkit.Sound
import org.bukkit.entity.Damageable
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.util.RayTraceResult

class VoidDagger : AbilityHandler() {

  override val primaryDescription: AbilityDescription? = null
  override val secondaryDescription: AbilityDescription = AbilityDescription(
    "Attraction of the Void",
    arrayListOf(
      Component.text("Teleport behind an enemy within 20 blocks,").color(NamedTextColor.GRAY),
      Component.text("dealing ").color(NamedTextColor.GRAY)
        .append(
          Component.text("150% ").color(NamedTextColor.RED)
            .append(Elements.VOID.comp)
        )
        .append(Component.text(" damage.").color(NamedTextColor.GRAY))
    ),
    100
  )

  override fun onPrimary(player: Player): Boolean {
    return false
  }

  override fun onSecondary(player: Player): Boolean {
    val handler = PlayerHandler(player)
    if (handler.getCurrentMana() >= 100) {val res = player.world.rayTrace(
      player.eyeLocation,
      player.eyeLocation.direction,
      20.0,
      FluidCollisionMode.NEVER,
      true,
      1.0
    ) { e -> e.type != EntityType.PLAYER && e is Damageable }
      if (res is RayTraceResult) {
        if (res.hitEntity is Damageable) {
          val entity = res.hitEntity as Damageable
          val entloc = entity.location
          var ret: Boolean
          try {
            ret = EntityHandler(entity).damage(handler.getDamage().toDouble())
          } catch (e: Exception) {
            ret = entity.health <= handler.getDamage()
            entity.damage(handler.getDamage().toDouble() * 1.5)
          }
          player.teleport(entloc)
          handler.setCurrentMana(handler.getCurrentMana() - 100)
          player.playSound(entloc, Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f)
          player.playSound(entloc, Sound.ENTITY_PLAYER_ATTACK_CRIT, 1.0f, 1.0f)
          return ret
        }
      }
    } else {
      player.sendMessage(Component.text("You don't have enough mana!").color(NamedTextColor.RED))
      player.playSound(player.location, Sound.ENTITY_ENDER_EYE_DEATH, 1.0f, 0.5f)
      return false
    }
    return false
  }
}