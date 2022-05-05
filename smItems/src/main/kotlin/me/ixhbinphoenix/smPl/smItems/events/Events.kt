package me.ixhbinphoenix.smPl.smItems.events

import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent
import me.ixhbinphoenix.smPl.smEntities.entities.damage
import me.ixhbinphoenix.smPl.smEntities.entities.showDamage
import me.ixhbinphoenix.smPl.smItems.Elements
import me.ixhbinphoenix.smPl.smItems.Types
import me.ixhbinphoenix.smPl.smItems.getInstance
import me.ixhbinphoenix.smPl.smItems.item.ArmorLoreRefresh
import me.ixhbinphoenix.smPl.smItems.item.EquipmentHandler
import me.ixhbinphoenix.smPl.smItems.item.ClickLoreRefresh
import me.ixhbinphoenix.smPl.smItems.item.ItemUtils
import me.ixhbinphoenix.smPl.smItems.item.abilities.Abilities
import me.ixhbinphoenix.smPl.smItems.item.abilities.AbilityHandler
import me.ixhbinphoenix.smPl.smItems.item.abilities.ProjectileAbilityHandler
import net.kyori.adventure.text.Component
import org.bukkit.*
import org.bukkit.attribute.Attribute
import org.bukkit.block.Block
import org.bukkit.entity.Damageable
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
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
import org.bukkit.persistence.PersistentDataType
import kotlin.math.roundToInt

class Events : Listener {
  private val plugin = getInstance()
  private val abilties = Abilities()
  private val itemUtils = ItemUtils()

  @EventHandler
  fun onPlayerEquip(event: PlayerItemHeldEvent) {
    StatsCalculation(event.player).runTaskLater(plugin, 2)
  }

  @EventHandler
  fun onInventoryClick(event: InventoryClickEvent){
    // More like event.whoAsked
    StatsCalculation(event.whoClicked as Player, true).runTaskLater(plugin, 1)
    if (event.clickedInventory == event.whoClicked.inventory) {
      ClickLoreRefresh(event).runTaskLater(plugin, 1)
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
  fun onPlayerArmorEquip(event: PlayerArmorChangeEvent) {
    StatsCalculation(event.player).runTaskLater(plugin, 1)
    ArmorLoreRefresh(event).runTaskLater(plugin, 1)
  }

  @EventHandler
  fun onEntityHurt(event: EntityDamageEvent){
    if (event.entity is Damageable) {
      if(event.cause != EntityDamageEvent.DamageCause.ENTITY_ATTACK && event.cause != EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK && event.cause != EntityDamageEvent.DamageCause.CUSTOM){
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
        if (itemUtils.isEquipment(damager.inventory.itemInMainHand)) {
          val equip = EquipmentHandler(damager.inventory.itemInMainHand, damager)
          if (abilties.getHandler(equip.id) is AbilityHandler) {
            val handler = abilties.getHandler(equip.id)
            handler!!.onPrimary(damager)
          } else {
            val id = if (equip.element is Elements) {
              "${equip.element}_${equip.type}"
            } else {
              "${equip.type}"
            }
            val handler = abilties.getHandler(id)
            handler!!.onPrimary(damager)
          }
          if (!equip.type.melee) {
            event.isCancelled = true
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
    }
    else{
      damage(event.entity as Damageable, event.damage)
    }
  }

  @EventHandler
  fun onPlayerClick(event: PlayerInteractEvent) {
    if (event.hasItem()) {
      if (event.item!!.hasItemMeta() && itemUtils.isEquipment(event.item!!)) {
        val equip = EquipmentHandler(event.item!!, event.player)
        if (event.action == Action.LEFT_CLICK_AIR || event.action == Action.LEFT_CLICK_BLOCK) {
          if (abilties.getHandler(equip.id) is AbilityHandler) {
            val handler = abilties.getHandler(equip.id)
            handler!!.onPrimary(event.player)
          } else {
            val id = if (equip.element is Elements) {
              "${equip.element}_${equip.type}"
            } else {
              "${equip.type}"
            }
            val handler = abilties.getHandler(id)
            if (handler is AbilityHandler) {
              handler.onPrimary(event.player)
            }
          }
          event.isCancelled = true
        } else if (event.action == Action.RIGHT_CLICK_AIR || event.action == Action.RIGHT_CLICK_BLOCK) {
          if (abilties.getHandler(equip.id) is AbilityHandler) {
            val handler = abilties.getHandler(equip.id)
            handler!!.onSecondary(event.player)
          } else {
            val id = if (equip.element is Elements) {
              "${equip.element}_${equip.type}"
            } else {
              "${equip.type}"
            }
            val handler = abilties.getHandler(id)
            if (handler is AbilityHandler) {
              handler.onSecondary(event.player)
            }
          }
        }
      }
    }
  }

  @EventHandler
  fun onProjectileCollision(event: ProjectileHitEvent) {
    val projectile = event.entity
    if (projectile.persistentDataContainer.has(NamespacedKey.fromString("smitems:projectile.type.str")!!)) {
      val id = projectile.persistentDataContainer.get(NamespacedKey.fromString("smitems:projectile.type.str")!!, PersistentDataType.STRING)!!.split("_")
      if (abilties.getHandler(id.dropLast(1).joinToString("_")) is ProjectileAbilityHandler) {
        if (id.last() == "PRIMARY") {
          val handler = abilties.getHandler(id.dropLast(1).joinToString("_"))!! as ProjectileAbilityHandler
          if (event.hitBlock is Block) {
            handler.onPrimaryCollision(event.hitBlock!!, projectile)
          } else if (event.hitEntity is Damageable) {
            handler.onPrimaryCollision(event.hitEntity!! as Damageable, projectile)
          }
        } else if (id.last() == "SECONDARY") {
          val handler = abilties.getHandler(id.dropLast(1).joinToString("_"))!! as ProjectileAbilityHandler
          if (event.hitBlock is Block) {
            handler.onSecondaryCollision(event.hitBlock!!, projectile)
          } else if (event.hitEntity is Damageable) {
            handler.onSecondaryCollision(event.hitEntity!! as Damageable, projectile)
          }
        }
      }
    }
  }

}