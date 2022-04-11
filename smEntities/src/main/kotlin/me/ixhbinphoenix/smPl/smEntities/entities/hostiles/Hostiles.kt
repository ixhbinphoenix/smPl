package me.ixhbinphoenix.smPl.smEntities.entities.hostiles

import me.ixhbinphoenix.smPl.smEntities.Type
import me.ixhbinphoenix.smPl.smEntities.entities.SmEntity

abstract class Hostiles() : SmEntity() {
    override val type = Type.HOSTILE
}