package me.ixhbinphoenix.smPl.smCore.entities

import org.bukkit.entity.Damageable
import org.bukkit.entity.EntityType
import org.bukkit.entity.Zombie
import org.bukkit.entity.LivingEntity
import org.bukkit.NamespacedKey
import org.bukkit.persistence.PersistentDataType
import org.bukkit.World
import org.bukkit.Location
import org.bukkit.attribute.Attribute
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor

open class EntityHandler(val entity: Damageable) {
    val id: String
    val xp: Int
    val type: EntityPosition
    val name: String

    init {
        this.name = getCustomName()
        this.id = getEntityID()
        this.xp = getEntityXP()
        this.type = getEntityType()
        updateName()
    }

    companion object {
        fun spawn(id: String, health: Double, damage: Double, xp: Int, type: EntityPosition, name: String, entityType: EntityType, world: World, location: Location): EntityHandler {
            val entity = world.spawnEntity(location, entityType) as LivingEntity
            entity.getAttribute(Attribute.GENERIC_MAX_HEALTH)!!.baseValue = health
            entity.health = health
            if (entityType == EntityType.ZOMBIE) (entity as Zombie).setShouldBurnInDay(false)
            entity.persistentDataContainer.set(NamespacedKey.fromString("smentities:entity.id.str")!!, PersistentDataType.STRING, id)
            entity.persistentDataContainer.set(NamespacedKey.fromString("smentities:entity.type.str")!!, PersistentDataType.STRING, type.toString())
            entity.persistentDataContainer.set(NamespacedKey.fromString("smentities:entity.damage.dou")!!, PersistentDataType.DOUBLE, damage)
            entity.persistentDataContainer.set(NamespacedKey.fromString("smentities:entity.xp.int")!!, PersistentDataType.INTEGER, xp)
            entity.persistentDataContainer.set(NamespacedKey.fromString("smentities:entity.name.str")!!, PersistentDataType.STRING, name)
            return EntityHandler(entity)
        }
        fun spawn(entityData: CustomEntity, world: World, location: Location): EntityHandler {
            val entity = world.spawnEntity(location, entityData.entityType) as LivingEntity
            entity.getAttribute(Attribute.GENERIC_MAX_HEALTH)!!.baseValue = entityData.health
            entity.health = entityData.health
            if (entityData.entityType == EntityType.ZOMBIE) (entity as Zombie).setShouldBurnInDay(false)
            entity.persistentDataContainer.set(NamespacedKey.fromString("smentities:entity.id.str")!!, PersistentDataType.STRING, entityData.id)
            entity.persistentDataContainer.set(NamespacedKey.fromString("smentities:entity.type.str")!!, PersistentDataType.STRING, entityData.type.toString())
            entity.persistentDataContainer.set(NamespacedKey.fromString("smentities:entity.damage.dou")!!, PersistentDataType.DOUBLE, entityData.damage)
            entity.persistentDataContainer.set(NamespacedKey.fromString("smentities:entity.xp.int")!!, PersistentDataType.INTEGER, entityData.xp)
            entity.persistentDataContainer.set(NamespacedKey.fromString("smentities:entity.name.str")!!, PersistentDataType.STRING, entityData.name)
            return EntityHandler(entity)
        }
    }

    fun updateName() {
        entity.customName(
            Component.text(name).color(type.color)
            .append(Component.text(" ${entity.health.toInt()}").color(NamedTextColor.GREEN)
                .append(Component.text("♥").color(NamedTextColor.RED))
            )
        )
    }

    fun damage(damage: Double): Boolean {
        val ret = damage >= entity.health
        entity.damage(damage)
        updateName()
        return ret
    }

    private fun getCustomName(): String {
        return entity.persistentDataContainer.getOrDefault(NamespacedKey.fromString("smentities:entity.name.str")!!, PersistentDataType.STRING, entity.type.toString())
    }

    private fun getEntityID(): String {
        return entity.persistentDataContainer.getOrDefault(NamespacedKey.fromString("smentities:entity.id.str")!!, PersistentDataType.STRING, "invalid_id")
    }

    fun getDamage(): Double {
        return entity.persistentDataContainer.getOrDefault(NamespacedKey.fromString("smentities:entity.damage.dou")!!, PersistentDataType.DOUBLE, 0.0)
    }

    fun setDamage(damage: Double) {
        entity.persistentDataContainer.set(NamespacedKey.fromString("smentities:entity.damage.dou")!!, PersistentDataType.DOUBLE, damage)
    }

    private fun getEntityXP(): Int {
        return entity.persistentDataContainer.getOrDefault(NamespacedKey.fromString("smentities:entity.xp.int")!!, PersistentDataType.INTEGER, 0)
    }

    private fun getEntityType(): EntityPosition {
        return EntityPosition.valueOf(entity.persistentDataContainer.getOrDefault(NamespacedKey.fromString("smentities:entity.type.str")!!, PersistentDataType.STRING, "HOSTILE"))
    }
}
