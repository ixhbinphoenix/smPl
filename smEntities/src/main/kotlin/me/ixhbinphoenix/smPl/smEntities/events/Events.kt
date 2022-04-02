package me.ixhbinphoenix.smPl.smEntities.events

import me.ixhbinphoenix.smPl.smEntities.Main
import me.ixhbinphoenix.smPl.smEntities.entities.updateName
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.NamespacedKey
import org.bukkit.entity.Damageable
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.persistence.PersistentDataType
import kotlin.math.roundToInt

class Events : Listener {

    @EventHandler
    fun onEntityHurt(event: EntityDamageEvent){
        if(event.cause != EntityDamageEvent.DamageCause.ENTITY_ATTACK){
            showDamage(event.entity as Damageable, event.damage)
        }
    }

    @EventHandler
    fun onEntityAttack(event: EntityDamageByEntityEvent){
        if(event.damager !is Player && event.entity is Player){
            val player = event.entity as Player
            val damage = event.damager.persistentDataContainer.getOrDefault(
                NamespacedKey(Main.instance, "entity.damage.dou"),
                PersistentDataType.DOUBLE,
                event.damage
            )
            var maxHealth = event.entity.persistentDataContainer.getOrDefault(
                NamespacedKey(me.ixhbinphoenix.smPl.smCore.Main.instance, "player.maxhealth.int"),
                PersistentDataType.INTEGER,
                20)
            if(maxHealth <= 20) maxHealth = 20
            event.damage = 20.0 * (damage / maxHealth)
            player.sendMessage(Component.text("Health: ${((player.health - (20.0 * (damage / maxHealth))) * (maxHealth / 20)).roundToInt()}/$maxHealth")
                .color(NamedTextColor.RED))
        }
        else if (event.damager is Player){
            val damage = event.damager.persistentDataContainer.getOrDefault(
                NamespacedKey(me.ixhbinphoenix.smPl.smCore.Main.instance, "player.damage.int"),
                PersistentDataType.INTEGER,
                event.damage.toInt()
            )
            event.damage = damage.toDouble()
            showDamage(event.entity as Damageable, event.damage)
        }
        else{
            showDamage(event.entity as Damageable, event.damage)
        }
    }

    fun showDamage(damageable: Damageable, damage: Double){
        val health: Double = if(damageable.health - damage > 0) damageable.health - damage else 0.0
        updateName(damageable, health, damageable.persistentDataContainer.getOrDefault(
            NamespacedKey(Main.instance, "entity.name.str"),
            PersistentDataType.STRING,
            "Damageable"
        ))
    }
}