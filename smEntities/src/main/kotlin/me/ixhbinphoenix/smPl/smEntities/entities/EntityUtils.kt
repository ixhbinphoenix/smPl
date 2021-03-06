package me.ixhbinphoenix.smPl.smEntities.entities

import me.ixhbinphoenix.smPl.smEntities.entities.hostiles.Zombie
import me.ixhbinphoenix.smPl.smEntities.Type
import me.ixhbinphoenix.smPl.smEntities.entities.BaseEntityHandler
import org.bukkit.NamespacedKey
import org.bukkit.entity.Damageable
import org.bukkit.entity.EntityType
import org.bukkit.persistence.PersistentDataType


data class CustomEntity(val id: String, val health: Double, val damage: Double, val xp: Int, val type: Type, val name: String, val entityType: EntityType)

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
