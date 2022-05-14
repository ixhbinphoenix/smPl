package me.ixhbinphoenix.smPl.smEntities.entities.hostiles

import org.bukkit.entity.EntityType
import me.ixhbinphoenix.smPl.smEntities.entities.BaseEntityHandler
import me.ixhbinphoenix.smPl.smEntities.entities.CustomEntity
import me.ixhbinphoenix.smPl.smEntities.Type

class Zombie : BaseEntityHandler() {
    override val data = CustomEntity(
        "zombie",
       1000.0,
       100.0,
       10,
       Type.HOSTILE,
       "Zombie",
       EntityType.ZOMBIE
    )
}
