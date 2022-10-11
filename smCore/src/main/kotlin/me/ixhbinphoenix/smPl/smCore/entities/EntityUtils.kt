package me.ixhbinphoenix.smPl.smCore.entities

import me.ixhbinphoenix.smPl.smCore.entities.hostiles.Zombie
import org.bukkit.NamespacedKey
import org.bukkit.entity.Damageable
import org.bukkit.entity.EntityType
import org.bukkit.entity.LivingEntity
import org.bukkit.persistence.PersistentDataType


data class CustomEntity(val id: String, val health: Double, val damage: Double, val xp: Int, val stance: EntityStance, val name: String, val entityType: EntityType)

class EntityUtils {
    companion object {
        @JvmStatic
        fun isCustomEntity(entity: Damageable): Boolean {
            return entity.persistentDataContainer.has(NamespacedKey.fromString("smentities:entity.id.str")!!, PersistentDataType.STRING)
        }
    }
    val entities = HashMap<String, BaseEntityHandler>()
    
    init {
        entities["zombie"] = Zombie()
    }
}

/**
 * Disables shouldBurnInDay on Entities that have it, or returns the Entity as is
 * @param entity
 * @return Same Entity, but with shouldBurnInDay disables
 */
fun disableBurn(entity: LivingEntity): LivingEntity {
    // This doesn't have to be exhaustive since we only cover cases where the entity can burn in Daylight
    // Bukkit should add some Abstract BurnInDayBikeshedEntity class or Java should have a trait System like rust
    @Suppress("NON_EXHAUSTIVE_WHEN_STATEMENT")
    when (entity.type) {
        EntityType.ZOMBIE -> (entity as org.bukkit.entity.Zombie).setShouldBurnInDay(false)
        EntityType.SKELETON -> (entity as org.bukkit.entity.Skeleton).setShouldBurnInDay(false)
        EntityType.PHANTOM -> (entity as org.bukkit.entity.Phantom).setShouldBurnInDay(false)
    }
    return entity
}