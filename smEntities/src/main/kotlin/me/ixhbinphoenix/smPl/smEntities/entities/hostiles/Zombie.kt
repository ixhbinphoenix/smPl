package me.ixhbinphoenix.smPl.smEntities.entities.hostiles

import me.ixhbinphoenix.smPl.smCore.commands.BaseCommand
import me.ixhbinphoenix.smPl.smEntities.entities.updateName
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Damageable
import org.bukkit.entity.Entity
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player

class Zombie : Hostiles(), CommandExecutor, BaseCommand {
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
        zombie.maxHealth = health
        zombie.health = health
        zombie.setShouldBurnInDay(false)
        saveDamage(damage)
        saveHealth(health)
        saveName(name)
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if(label == "zombie"){
            if(sender is Player){
                spawn(sender.world, sender.location)
                return true
            }
        }
        return false
    }

    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>
    ): MutableList<String>? {
        return null
    }
}