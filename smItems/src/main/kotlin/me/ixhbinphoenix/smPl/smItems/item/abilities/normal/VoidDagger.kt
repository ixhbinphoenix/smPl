package me.ixhbinphoenix.smPl.smItems.item.abilities.normal

import me.ixhbinphoenix.smPl.smCore.player.PlayerHandler
import me.ixhbinphoenix.smPl.smCore.entities.EntityHandler
import me.ixhbinphoenix.smPl.smItems.item.abilities.AbilityHandler
import org.bukkit.FluidCollisionMode
import org.bukkit.Sound
import org.bukkit.entity.Damageable
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.util.RayTraceResult

class VoidDagger : AbilityHandler() {
  override fun onPrimary(player: Player): Boolean {return false}

  override fun onSecondary(player: Player): Boolean {
    val res = player.world.rayTrace(player.eyeLocation, player.eyeLocation.direction, 20.0, FluidCollisionMode.NEVER, true, 1.0) { e -> e.type != EntityType.PLAYER && e is Damageable }
    if (res is RayTraceResult) {
      if (res.hitEntity is Damageable) {
        val handler = PlayerHandler(player)
        val entity = res.hitEntity as Damageable
        val entloc = entity.location
        var ret: Boolean
        try {
          ret = EntityHandler(entity).damage(handler.getDamage().toDouble())
        } catch (e: Exception) {
          ret = entity.health <= handler.getDamage()
          entity.damage(handler.getDamage().toDouble())
        }
        player.teleport(entloc)
        player.playSound(entloc, Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f)
        player.playSound(entloc, Sound.ENTITY_PLAYER_ATTACK_CRIT, 1.0f, 1.0f)
        return ret
      }
    }
    return false
  }
}
