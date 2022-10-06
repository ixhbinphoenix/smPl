package me.ixhbinphoenix.smPl.smEntities.entities

import me.ixhbinphoenix.smPl.smCore.entities.CustomEntity

abstract class BaseEntityHandler {
    abstract val data: CustomEntity
    // TODO: Abstract functions for "lifecycle-hooks" (e.g. onDamage, onDeath, etc)
}
