package me.ixhbinphoenix.smPl.smEntities.entities.hostiles

import me.ixhbinphoenix.smPl.smEntities.entities.updateName
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Damageable
import org.bukkit.entity.Entity
import org.bukkit.entity.EntityType

class Zombie : Hostiles() {
    override val name = "Zombie"
    override val damage = 100.0
    override val grantingXp = 10
    override var health = 1000.0
    override val entityType = EntityType.ZOMBIE
    override var entity: Entity? = null

    fun spawn(world: World, location: Location) {
        entity = world.spawnEntity(location, entityType)
        updateName(entity as Damageable, health, name)
        val zombie = entity as org.bukkit.entity.Zombie
        zombie.getAttribute(Attribute.GENERIC_MAX_HEALTH)!!.baseValue = health
        zombie.health = health
        zombie.setShouldBurnInDay(false)
        saveDamage(damage)
        saveHealth(health)
        saveName(name)
    }
}