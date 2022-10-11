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
import org.bukkit.entity.Phantom
import org.bukkit.entity.Skeleton

/**
 * Handles any custom Entity
 * @param entity
 * @property id Custom Entity ID
 * @property xp How much xp the Entity grants on kill
 */
@Suppress("MemberVisibilityCanBePrivate")
open class EntityHandler(val entity: Damageable) {
    val id: String
    val xp: Int
    val stance: EntityStance
    val name: String

    init {
        this.name = getCustomName()
        this.id = getEntityID()
        this.xp = getEntityXP()
        this.stance = getEntityStance()
        updateName()
    }

    companion object {
        /**
         * Spawns an entity and creates a Handler for it
         * @param id Custom Entity ID
         * @param health Max Health
         * @param damage Damage the Entity does
         * @param xp Granted XP
         * @param stance Stance towards the player
         * @param name Custom name
         * @param entityType Minecraft Entity
         * @param world World where the entity should be spawned
         * @param location Location of the Entity
         */
        fun spawn(id: String, health: Double, damage: Double, xp: Int, stance: EntityStance, name: String, entityType: EntityType, world: World, location: Location): EntityHandler {
            var entity = world.spawnEntity(location, entityType) as LivingEntity
            entity.getAttribute(Attribute.GENERIC_MAX_HEALTH)!!.baseValue = health
            entity.health = health
            entity = disableBurn(entity)
            entity.persistentDataContainer.set(NamespacedKey.fromString("smentities:entity.id.str")!!, PersistentDataType.STRING, id)
            entity.persistentDataContainer.set(NamespacedKey.fromString("smentities:entity.type.str")!!, PersistentDataType.STRING, stance.toString())
            entity.persistentDataContainer.set(NamespacedKey.fromString("smentities:entity.damage.dou")!!, PersistentDataType.DOUBLE, damage)
            entity.persistentDataContainer.set(NamespacedKey.fromString("smentities:entity.xp.int")!!, PersistentDataType.INTEGER, xp)
            entity.persistentDataContainer.set(NamespacedKey.fromString("smentities:entity.name.str")!!, PersistentDataType.STRING, name)
            return EntityHandler(entity)
        }

        /**
         * Spawns an entity and creates a Handler for it
         * @param entityData Custom Entity data
         * @param world World of the Entity
         * @param location Location of the Entity
         */
        fun spawn(entityData: CustomEntity, world: World, location: Location): EntityHandler {
            var entity = world.spawnEntity(location, entityData.entityType) as LivingEntity
            entity.getAttribute(Attribute.GENERIC_MAX_HEALTH)!!.baseValue = entityData.health
            entity.health = entityData.health
            entity = disableBurn(entity)
            entity.persistentDataContainer.set(NamespacedKey.fromString("smentities:entity.id.str")!!, PersistentDataType.STRING, entityData.id)
            entity.persistentDataContainer.set(NamespacedKey.fromString("smentities:entity.type.str")!!, PersistentDataType.STRING, entityData.stance.toString())
            entity.persistentDataContainer.set(NamespacedKey.fromString("smentities:entity.damage.dou")!!, PersistentDataType.DOUBLE, entityData.damage)
            entity.persistentDataContainer.set(NamespacedKey.fromString("smentities:entity.xp.int")!!, PersistentDataType.INTEGER, entityData.xp)
            entity.persistentDataContainer.set(NamespacedKey.fromString("smentities:entity.name.str")!!, PersistentDataType.STRING, entityData.name)
            return EntityHandler(entity)
        }
    }

    /**
     * Updates stance color and Health in displayname
     */
    fun updateName() {
        entity.customName(
            Component.text(name).color(stance.color)
            .append(Component.text(" ${entity.health.toInt()}").color(NamedTextColor.GREEN)
                .append(Component.text("♥").color(NamedTextColor.RED))
            )
        )
    }

    /**
     * Damage the Entity
     * @param damage Amount of Damage
     * @return If the Entity got killed
     */
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

    private fun getEntityStance(): EntityStance {
        return EntityStance.valueOf(entity.persistentDataContainer.getOrDefault(NamespacedKey.fromString("smentities:entity.type.str")!!, PersistentDataType.STRING, "HOSTILE"))
    }
}
