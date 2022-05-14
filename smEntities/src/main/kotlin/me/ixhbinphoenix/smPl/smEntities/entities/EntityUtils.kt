package me.ixhbinphoenix.smPl.smEntities.entities

import me.ixhbinphoenix.smPl.smEntities.entities.hostiles.Zombie
import me.ixhbinphoenix.smPl.smEntities.Type
import me.ixhbinphoenix.smPl.smEntities.entities.BaseEntityHandler
import org.bukkit.entity.EntityType


data class CustomEntity(val id: String, val health: Double, val damage: Double, val xp: Int, val type: Type, val name: String, val entityType: EntityType)

class EntityUtils {
    val entities = HashMap<String, BaseEntityHandler>()
    
    init {
        entities["zombie"] = Zombie()
    }
}
