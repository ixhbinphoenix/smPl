package me.ixhbinphoenix.smPl.smCore.events

import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent
import me.ixhbinphoenix.smPl.smCore.entities.EntityHandler
import me.ixhbinphoenix.smPl.smCore.entities.EntityUtils
import me.ixhbinphoenix.smPl.smCore.getInstance
import me.ixhbinphoenix.smPl.smCore.items.*
import me.ixhbinphoenix.smPl.smCore.items.abilities.AbilityHandler
import me.ixhbinphoenix.smPl.smCore.items.abilities.ProjectileAbilityHandler
import me.ixhbinphoenix.smPl.smCore.items.abilities.Abilities
import org.bukkit.NamespacedKey
import org.bukkit.attribute.Attribute
import org.bukkit.block.Block
import org.bukkit.entity.Damageable
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityPickupItemEvent
import org.bukkit.event.entity.ProjectileHitEvent
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.player.*
import org.bukkit.persistence.PersistentDataType
import java.util.UUID
import kotlin.math.roundToInt

class Events : Listener {
  private val runnableMap = HashMap<UUID, PlayerSecond>()
  private val plugin = getInstance()
  private val abilities = Abilities()

  @EventHandler
  fun onPlayerJoin(event: PlayerJoinEvent) {
    runnableMap[event.player.uniqueId] = PlayerSecond(event.player)
    runnableMap[event.player.uniqueId]!!.runTaskTimerAsynchronously(plugin, 0, 20)
  }

  @EventHandler
  fun onPlayerLeave(event: PlayerQuitEvent) {
    runnableMap[event.player.uniqueId]!!.cancel()
  }

  @EventHandler
  fun onPlayerEquip(event: PlayerItemHeldEvent) {
    StatsCalculation(event.player).runTaskLater(plugin, 2)
  }

  @EventHandler
  fun onInventoryClick(event: InventoryClickEvent) {
    StatsCalculation(event.whoClicked as Player, true).runTaskLater(plugin, 1)
    if (event.clickedInventory == event.whoClicked.inventory) {
      ClickLoreRefresh(event).runTaskLater(plugin, 1)
    }
  }

  @EventHandler
  fun onPlayerPickUp(event: EntityPickupItemEvent) {
    if (event.entity is Player) {
      StatsCalculation(event.entity as Player).runTaskLater(plugin, 1)
    }
  }

  @EventHandler
  fun onPlayerDrop(event: PlayerDropItemEvent) {
    StatsCalculation(event.player).runTaskLater(plugin, 1)
  }

  @EventHandler
  fun onPlayerArmorEquip(event: PlayerArmorChangeEvent) {
    StatsCalculation(event.player).runTaskLater(plugin, 1)
    ArmorLoreRefresh(event).runTaskLater(plugin, 1)
  }

  @EventHandler
  fun onEntityHurt(event: EntityDamageEvent) {
    if (event.entity is Damageable) {
      if (event.cause != EntityDamageEvent.DamageCause.ENTITY_ATTACK && event.cause != EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK && event.cause != EntityDamageEvent.DamageCause.CUSTOM) {
        EntityHandler(event.entity as Damageable).updateName()
      }
      if (event.cause == EntityDamageEvent.DamageCause.FIRE_TICK) {
        event.isCancelled = false
      }
    }
  }

  @EventHandler
  fun onEntityAttack(event: EntityDamageByEntityEvent) {
    event.damage = 0.0
    if (event.damager !is Player && event.entity is Player) {
      val player = event.entity as Player
      val damage = event.damager.persistentDataContainer.getOrDefault(
        NamespacedKey.fromString("smcore:entity.damage.dou")!!,
        PersistentDataType.DOUBLE,
        event.damage
      )
      val vanillaMaxHealth = player.getAttribute(Attribute.GENERIC_MAX_HEALTH)?.value?.roundToInt() ?: 20
      var maxHealth = player.persistentDataContainer.getOrDefault(
        NamespacedKey.fromString("smcore:player.maxhealth.int")!!,
        PersistentDataType.INTEGER,
        vanillaMaxHealth
      )
      if (maxHealth <= 20) maxHealth = 20
      player.damage(vanillaMaxHealth.toDouble() * (damage / maxHealth))
    } else if (event.damager is Player) {
      if (event.cause == EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
        val damager = event.damager as Player
        if (ItemUtils.isEquipment(damager.inventory.itemInMainHand)) {
          val equip = EquipmentHandler(damager.inventory.itemInMainHand, damager)
          if (abilities.getHandler(equip.id) is AbilityHandler) {
            val handler = abilities.getHandler(equip.id)
            handler!!.onPrimary(damager)
          } else {
            val id = if (equip.element is Elements) {
              "${equip.element}_${equip.type}"
            } else {
              "${equip.type}"
            }
            val handler = abilities.getHandler(id)
            if (handler is AbilityHandler) {
              handler.onPrimary(damager)
            }
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
      if (EntityUtils.isCustomEntity(event.entity as Damageable)) {
        if (EntityHandler(event.entity as Damageable).damage(damage.toDouble())) {
          PlayerEquipHandler(event.damager as Player).grantXP(EntityHandler(event.entity as Damageable).xp)
        }
      } else {
        (event.entity as Damageable).damage(damage.toDouble())
      }
    } else {
      if (EntityUtils.isCustomEntity(event.entity as Damageable)) {
        if (EntityHandler(event.entity as Damageable).damage(event.damage)) {
          PlayerEquipHandler(event.damager as Player).grantXP(EntityHandler(event.entity as Damageable).xp)
        }
      } else {
        (event.entity as Damageable).damage(event.damage)
      }
    }
  }

  @EventHandler
  fun onPlayerClick(event: PlayerInteractEvent) {
    if (event.hasItem()) {
      if (event.item!!.hasItemMeta() && ItemUtils.isEquipment(event.item!!)) {
        val equip = EquipmentHandler(event.item!!, event.player)
        if (event.action == Action.LEFT_CLICK_AIR || event.action == Action.LEFT_CLICK_BLOCK) {
          if (abilities.getHandler(equip.id) is AbilityHandler) {
            val handler = abilities.getHandler(equip.id)
            handler!!.onPrimary(event.player)
          } else {
            val id = if (equip.element is Elements) {
              "${equip.element}_${equip.type}"
            } else {
              "${equip.type}"
            }
            val handler = abilities.getHandler(id)
            if (handler is AbilityHandler) {
              handler.onPrimary(event.player)
            }
          }
          event.isCancelled = true
        } else if (event.action == Action.RIGHT_CLICK_AIR || event.action == Action.RIGHT_CLICK_BLOCK) {
          if (abilities.getHandler(equip.id) is AbilityHandler) {
            val handler = abilities.getHandler(equip.id)
            handler!!.onSecondary(event.player)
          } else {
            val id = if (equip.element is Elements) {
              "${equip.element}_${equip.type}"
            } else {
              "${equip.type}"
            }
            val handler = abilities.getHandler(id)
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
    if (projectile.persistentDataContainer.has(NamespacedKey.fromString("smcore:projectile.type.str")!!)) {
      val id = projectile.persistentDataContainer.get(
        NamespacedKey.fromString("smcore:projectile.type.str")!!,
        PersistentDataType.STRING
      )!!.split("_")
      if (abilities.getHandler(id.dropLast(1).joinToString("_")) is ProjectileAbilityHandler) {
        if (id.last() == "PRIMARY") {
          val handler = abilities.getHandler(id.dropLast(1).joinToString("_"))!! as ProjectileAbilityHandler
          if (event.hitBlock is Block) {
            handler.onPrimaryCollision(event.hitBlock!!, projectile)
          } else if (event.hitEntity is Damageable) {
            if (handler.onPrimaryCollision(event.hitEntity!! as Damageable, projectile)) {
              if (EntityUtils.isCustomEntity(event.hitEntity!! as Damageable)) {
                val player = plugin.server.getPlayerExact(
                  projectile.persistentDataContainer.getOrDefault(
                    NamespacedKey.fromString("smcore:projectile.owner.str")!!, PersistentDataType.STRING, ""
                  )
                )
                if (player is Player) {
                  PlayerEquipHandler(player).grantXP(EntityHandler(event.hitEntity!! as Damageable).xp)
                }
              }
            }
          }
        } else if (id.last() == "SECONDARY") {
          val handler = abilities.getHandler(id.dropLast(1).joinToString("_"))!! as ProjectileAbilityHandler
          if (event.hitBlock is Block) {
            handler.onSecondaryCollision(event.hitBlock!!, projectile)
          } else if (event.hitEntity is Damageable) {
            if (handler.onSecondaryCollision(event.hitEntity!! as Damageable, projectile)) {
              if (EntityUtils.isCustomEntity(event.hitEntity!! as Damageable)) {
                val player = plugin.server.getPlayerExact(
                  projectile.persistentDataContainer.getOrDefault(
                    NamespacedKey.fromString("smcore:projectile.owner.str")!!, PersistentDataType.STRING, ""
                  )
                )
                if (player is Player) {
                  PlayerEquipHandler(player).grantXP(EntityHandler(event.hitEntity!! as Damageable).xp)
                }
              }
            }
          }
        }
      }
    }
  }
}