package me.ixhbinphoenix.smPl.smCore.player

import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.persistence.PersistentDataType

class PlayerHandler(player: Player) {
    private val player: Player
    init{
        this.player = player
    }

    fun setMana(mana: Int){
        player.persistentDataContainer.set(NamespacedKey.fromString("smcore:player.mana.int")!!,
            PersistentDataType.INTEGER,
            mana
        )
    }

    fun getMana(): Int{
        return player.persistentDataContainer.getOrDefault(NamespacedKey.fromString("smcore:player.mana.int")!!,
            PersistentDataType.INTEGER,
            0
        )
    }

    fun setDamage(damage: Int){
        player.persistentDataContainer.set(NamespacedKey.fromString("smcore:player.damage.int")!!,
            PersistentDataType.INTEGER,
            damage
        )
    }

    fun getDamage(): Int{
        return player.persistentDataContainer.getOrDefault(NamespacedKey.fromString("smcore:player.damage.int")!!,
            PersistentDataType.INTEGER,
            0
        )
    }
}