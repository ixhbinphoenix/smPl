package me.ixhbinphoenix.smPl.smCore.items.abilities

import org.bukkit.block.Block
import org.bukkit.entity.Damageable
import org.bukkit.entity.Projectile

/**
 * AbilityHandler for Abilities with Projectiles
 */
abstract class ProjectileAbilityHandler : AbilityHandler() {
  abstract fun onPrimaryCollision(hit: Damageable, projectile: Projectile): Boolean
  abstract fun onPrimaryCollision(hit: Block, projectile: Projectile): Boolean
  abstract fun onSecondaryCollision(hit: Damageable, projectile: Projectile): Boolean
  abstract fun onSecondaryCollision(hit: Block, projectile: Projectile): Boolean
}