package me.ixhbinphoenix.smPl.smItems.item.abilities

import org.bukkit.block.Block
import org.bukkit.entity.Damageable
import org.bukkit.entity.Projectile

abstract class ProjectileAbilityHandler : AbilityHandler() {
  abstract fun onPrimaryCollision(hit: Damageable, projectile: Projectile): Boolean
  abstract fun onPrimaryCollision(hit: Block, projectile: Projectile): Boolean
  abstract fun onSecondaryCollision(hit: Damageable, projectile: Projectile): Boolean
  abstract fun onSecondaryCollision(hit: Block, projectile: Projectile): Boolean
}