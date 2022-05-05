package me.ixhbinphoenix.smPl.smItems.item.abilities.normal

import me.ixhbinphoenix.smPl.smCore.player.PlayerHandler
import me.ixhbinphoenix.smPl.smEntities.entities.damage
import me.ixhbinphoenix.smPl.smItems.item.abilities.ProjectileAbilityHandler
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.Particle
import org.bukkit.block.Block
import org.bukkit.entity.Damageable
import org.bukkit.entity.Player
import org.bukkit.entity.Projectile
import org.bukkit.entity.Snowball
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

class FireBook : ProjectileAbilityHandler() {
  override fun onPrimary(player: Player) {
    val handler = PlayerHandler(player)
    val projectile = player.launchProjectile(Snowball::class.java)
    projectile.item = ItemStack(Material.FIRE_CHARGE)
    projectile.persistentDataContainer.set(NamespacedKey.fromString("smitems:projectile.type.str")!!, PersistentDataType.STRING, "FIRE_BOOK_PRIMARY")
    projectile.persistentDataContainer.set(NamespacedKey.fromString("smitems:projectile.owner.str")!!, PersistentDataType.STRING, player.name)
    projectile.persistentDataContainer.set(NamespacedKey.fromString("smitems:projectile.damage.int")!!, PersistentDataType.INTEGER, handler.getDamage())
  }

  override fun onPrimaryCollision(hit: Damageable, projectile: Projectile) {
    val location = hit.location
    val damage = projectile.persistentDataContainer.getOrDefault(NamespacedKey.fromString("smitems:projectile.damage.int")!!, PersistentDataType.INTEGER, 0)
    damage(hit, damage.toDouble())
    location.world.spawnParticle(Particle.LAVA, location, 5)
    projectile.remove()
  }

  override fun onPrimaryCollision(hit: Block, projectile: Projectile) {
    val location = hit.location
    location.y += 1
    location.world.spawnParticle(Particle.LAVA, location, 5)
    projectile.remove()
  }

  override fun onSecondary(player: Player) {}
  override fun onSecondaryCollision(hit: Block, projectile: Projectile) {}
  override fun onSecondaryCollision(hit: Damageable, projectile: Projectile) {}
}