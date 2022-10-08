package me.ixhbinphoenix.smPl.smCore.entities.hostiles

import org.bukkit.entity.EntityType
import me.ixhbinphoenix.smPl.smCore.entities.BaseEntityHandler
import me.ixhbinphoenix.smPl.smCore.entities.CustomEntity
import me.ixhbinphoenix.smPl.smCore.entities.EntityPosition

class Zombie : BaseEntityHandler() {
    override val data = CustomEntity(
        "zombie",
       1000.0,
       100.0,
       10,
       EntityPosition.HOSTILE,
       "Zombie",
       EntityType.ZOMBIE
    )
}
