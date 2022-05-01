package me.ixhbinphoenix.smPl.smEntities.entities.hostiles

import me.ixhbinphoenix.smPl.smEntities.entities.updateName
import org.bukkit.Location
import org.bukkit.NamespacedKey
import org.bukkit.World
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Damageable
import org.bukkit.entity.Entity
import org.bukkit.entity.EntityType
import org.bukkit.persistence.PersistentDataType

class Zombie : Hostiles() {
    override val name = "Zombie"
    override val damage = 100.0
    override val grantingXp = 10
    override var health = 1000.0
    override val entityType = EntityType.ZOMBIE
    override var entity: Entity? = null

    override fun spawn(world: World, location: Location) {
        entity = world.spawnEntity(location, entityType)
        val zombie = entity as org.bukkit.entity.Zombie
        zombie.getAttribute(Attribute.GENERIC_MAX_HEALTH)!!.baseValue = health
        zombie.health = health
        zombie.setShouldBurnInDay(false)
        zombie.persistentDataContainer.set(NamespacedKey.fromString("smentities:entity.type.str")!!, PersistentDataType.STRING, "HOSTILE")
        saveDamage(damage)
        saveHealth(health)
        saveName(name)
        updateName(zombie, health, name)
    }
}