package me.ixhbinphoenix.smPl.smEntities.entities

import me.ixhbinphoenix.smPl.smEntities.Type
import me.ixhbinphoenix.smPl.smEntities.Main
import me.ixhbinphoenix.smPl.smEntities.getInstance
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Location
import org.bukkit.NamespacedKey
import org.bukkit.World
import org.bukkit.entity.Damageable
import org.bukkit.entity.Entity
import org.bukkit.entity.EntityType
import org.bukkit.persistence.PersistentDataType
import kotlin.math.roundToInt

abstract class SmEntity {
    abstract val name: String
    abstract val damage: Double
    abstract val health: Double
    abstract val type: Type
    abstract val grantingXp: Int
    abstract var entity: Entity?
    abstract val entityType: EntityType

    abstract fun spawn(world: World, location: Location)

    fun saveDamage(value: Double){
        if(entity != null){
            entity!!.persistentDataContainer.set(NamespacedKey(Main.instance, "entity.damage.dou"),
                PersistentDataType.DOUBLE,
                value
            )
        }
    }

    fun saveHealth(value: Double){
        if(entity != null){
            entity!!.persistentDataContainer.set(NamespacedKey(Main.instance, "entity.health.dou"),
                PersistentDataType.DOUBLE,
                value
            )
        }
    }

    fun saveName(value: String){
        if(entity != null){
            entity!!.persistentDataContainer.set(NamespacedKey(Main.instance, "entity.name.str"),
                PersistentDataType.STRING,
                value
            )
        }
    }

    fun getSavedDamage(): Double{
        return entity!!.persistentDataContainer.getOrDefault(NamespacedKey(Main.instance, "entity.damage.dou"),
            PersistentDataType.DOUBLE,
            0.0
        )
    }

    fun getSavedHealth(): Double{
        return entity!!.persistentDataContainer.getOrDefault(NamespacedKey(Main.instance, "entity.health.dou"),
            PersistentDataType.DOUBLE,
            0.0
        )
    }

    fun getSavedName(): String{
        return entity!!.persistentDataContainer.getOrDefault(NamespacedKey(Main.instance, "entity.name.str"),
            PersistentDataType.STRING,
            "undefined"
        )
    }

}

fun updateName(entity: Damageable, health: Double, name: String){
    val type = Type.valueOf(entity.persistentDataContainer.getOrDefault(NamespacedKey.fromString("smentities:entity.type.str")!!, PersistentDataType.STRING, "PASSIVE"))
    entity.customName(
        Component.text(name).color(type.color)
            .append(Component.text(" ${health.toInt()}").color(NamedTextColor.GREEN)
                .append(Component.text("♥").color(NamedTextColor.RED))
            )
    )
}
fun showDamage(damageable: Damageable, damage: Double){
    val health: Double = if ((damageable.health - damage) > 0) damageable.health - damage else 0.0
    updateName(damageable, health, damageable.persistentDataContainer.getOrDefault(
        NamespacedKey.fromString("smentities:entity.name.str")!!,
        PersistentDataType.STRING,
        damageable.type.toString()
    ))
}

fun showDamage(damageable: Damageable){
    updateName(damageable, damageable.health, damageable.persistentDataContainer.getOrDefault(
        NamespacedKey.fromString("smentities:entity.name.str")!!,
        PersistentDataType.STRING,
        damageable.type.toString()
    ))
}

fun damage(entity: Damageable, damage: Double) {
    entity.damage(damage)
    showDamage(entity)
}