package me.ixhbinphoenix.smPl.smItems.events

import me.ixhbinphoenix.smPl.smCore.player.PlayerHandler
import me.ixhbinphoenix.smPl.smEntities.entities.damage
import me.ixhbinphoenix.smPl.smEntities.entities.showDamage
import me.ixhbinphoenix.smPl.smItems.Main
import me.ixhbinphoenix.smPl.smItems.Types
import me.ixhbinphoenix.smPl.smItems.item.ItemHandler
import me.ixhbinphoenix.smPl.smItems.item.LoreRefresh
import org.bukkit.*
import org.bukkit.attribute.Attribute
import org.bukkit.block.Block
import org.bukkit.entity.Damageable
import org.bukkit.entity.Player
import org.bukkit.entity.Snowball
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityPickupItemEvent
import org.bukkit.event.entity.ProjectileHitEvent
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerItemHeldEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import kotlin.math.roundToInt

class Events : Listener {
  private val plugin = Bukkit.getPluginManager().getPlugin("smItems") as Main

  @EventHandler
  fun onPlayerEquip(event: PlayerItemHeldEvent) {
    StatsCalculation(event.player).runTaskLater(plugin, 2)
  }

  @EventHandler
  fun onInventoryClick(event: InventoryClickEvent){
    // More like event.whoAsked
    StatsCalculation(event.whoClicked as Player, true).runTaskLater(plugin, 1)
    if (event.clickedInventory == event.whoClicked.inventory) {
      LoreRefresh(event).runTaskLater(plugin, 1)
    }
  }

  @EventHandler
  fun onPlayerPickUp(event: EntityPickupItemEvent){
    if(event.entity is Player){
      StatsCalculation(event.entity as Player).runTaskLater(plugin, 1)
    }
  }

  @EventHandler
  fun onPlayerDrop(event: PlayerDropItemEvent){
    StatsCalculation(event.player).runTaskLater(plugin, 1)
  }

  @EventHandler
  fun onEntityHurt(event: EntityDamageEvent){
    if (event.entity is Damageable) {
      if(event.cause != EntityDamageEvent.DamageCause.ENTITY_ATTACK && event.cause != EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK){
        showDamage(event.entity as Damageable, event.damage)
      }
      if (event.cause == EntityDamageEvent.DamageCause.FIRE_TICK) {
        event.isCancelled = false
      }
    }
  }

  @EventHandler
  fun onEntityAttack(event: EntityDamageByEntityEvent){
    event.damage = 0.0
    if(event.damager !is Player && event.entity is Player){
      val player = event.entity as Player
      val damage = event.damager.persistentDataContainer.getOrDefault(
        NamespacedKey.fromString("smentities:entity.damage.dou")!!,
        PersistentDataType.DOUBLE,
        event.damage
      )
      val vanillaMaxHealth = player.getAttribute(Attribute.GENERIC_MAX_HEALTH)?.value?.roundToInt() ?: 20
      var maxHealth = player.persistentDataContainer.getOrDefault(
        NamespacedKey.fromString("smcore:player.maxhealth.int")!!,
        PersistentDataType.INTEGER,
        vanillaMaxHealth)
      if(maxHealth <= 20) maxHealth = 20
      damage(player, vanillaMaxHealth.toDouble() * (damage / maxHealth))
    }
    else if (event.damager is Player){
      if (event.cause == EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
        val damager = event.damager as Player
        if (damager.inventory.itemInMainHand.hasItemMeta()) {
          val handler = ItemHandler(damager.inventory.itemInMainHand, damager)
          if (handler.type == Types.BOOK) {
            event.isCancelled = true
            val projectile = damager.launchProjectile(Snowball::class.java)
            projectile.item = ItemStack(Material.FIRE_CHARGE)
            projectile.persistentDataContainer.set(NamespacedKey.fromString("smitems:projectile.type.str")!!, PersistentDataType.STRING, "${handler.type}_${handler.rarity}_${handler.element}_PRIMARY")
            projectile.persistentDataContainer.set(NamespacedKey.fromString("smitems:projectile.owner.str")!!, PersistentDataType.STRING, damager.name)
            projectile.persistentDataContainer.set(NamespacedKey.fromString("smitems:projectile.damage.int")!!, PersistentDataType.INTEGER, PlayerHandler(damager).getDamage())
            return
          }
        }
      }
      val damage = event.damager.persistentDataContainer.getOrDefault(
        NamespacedKey.fromString("smcore:player.damage.int")!!,
        PersistentDataType.INTEGER,
        event.damage.toInt()
      )
      damage(event.entity as Damageable, damage.toDouble())
      showDamage(event.entity as Damageable, damage.toDouble())
    }
    else{
      damage(event.entity as Damageable, event.damage)
      showDamage(event.entity as Damageable, event.damage)
    }
  }

  @EventHandler
  fun onPlayerClick(event: PlayerInteractEvent) {
    if (event.hasItem()) {
      if (event.item!!.hasItemMeta()) {
        val handler = ItemHandler(event.item!!, event.player)
        val player = PlayerHandler(event.player)
        if (handler.type == Types.BOOK) {
          if (event.action == Action.LEFT_CLICK_AIR || event.action == Action.LEFT_CLICK_BLOCK) {
            val projectile = event.player.launchProjectile(Snowball::class.java)
            projectile.item = ItemStack(Material.FIRE_CHARGE)
            projectile.persistentDataContainer.set(NamespacedKey.fromString("smitems:projectile.type.str")!!, PersistentDataType.STRING, "SATANS_TEACHINGS_PRIMARY")
            projectile.persistentDataContainer.set(NamespacedKey.fromString("smitems:projectile.owner.str")!!, PersistentDataType.STRING, event.player.name)
            projectile.persistentDataContainer.set(NamespacedKey.fromString("smitems:projectile.damage.int")!!, PersistentDataType.INTEGER, player.getDamage())
            event.isCancelled = true
          }
        }
      }
    }
  }

  @EventHandler
  fun onProjectileCollision(event: ProjectileHitEvent) {
    if (event.hitEntity is Damageable) {
      val location = event.hitEntity!!.location
      val projectile = event.entity
      if (event.entity.persistentDataContainer.has(NamespacedKey.fromString("smitems:projectile.type.str")!!)) {
        val damage = projectile.persistentDataContainer.getOrDefault(NamespacedKey.fromString("smitems:projectile.damage.int")!!, PersistentDataType.INTEGER, 0)
        damage(event.hitEntity!! as Damageable, damage.toDouble())
        location.world.spawnParticle(Particle.LAVA, location, 5)
      }
    } else if (event.hitBlock is Block) {
      val location = event.hitBlock!!.location
      location.y += 1
      if (event.entity.persistentDataContainer.has(NamespacedKey.fromString("smitems:projectile.type.str")!!)) {
        location.world.spawnParticle(Particle.LAVA, location, 5)
      }
    }
  }

}