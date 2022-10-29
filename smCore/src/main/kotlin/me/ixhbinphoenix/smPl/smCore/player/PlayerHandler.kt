package me.ixhbinphoenix.smPl.smCore.player

import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.persistence.PersistentDataType

/**
 * Gets and sets the data of a Player
 * @param player
 */
class PlayerHandler(private val player: Player) {
  @Suppress("JoinDeclarationAndAssignment")
  private val stats: ArrayList<String>

  init {
    this.stats = arrayListOf(
      "smcore:player.mana.int",
      "smcore:player.damage.int",
      "smcore:player.maxhealth.int",
      "smcore:player.defence.int"
    )
  }

  /**
   * Resets numerical stats of the player
   */
  fun reset() {
    for (stat in stats) {
      val keys = stat.split('.')
      when (keys[keys.size - 1]) {
        "int" -> {
          if (player.persistentDataContainer.has(NamespacedKey.fromString(stat)!!)) {
            player.persistentDataContainer.set(NamespacedKey.fromString(stat)!!, PersistentDataType.INTEGER, 0)
          }
        }
      }
    }
  }

  // TODO: Make this actually an internal function by merging smItems
  /**
   * Sets the MAXIMUM mana of a Player, for internal use
   * @param mana
   */
  fun setMana(mana: Int) {
    player.persistentDataContainer.set(
      NamespacedKey.fromString("smcore:player.mana.int")!!,
      PersistentDataType.INTEGER,
      mana
    )
  }

  /**
   * Gets the MAXIMUM mana of a Player
   * @return mana
   */
  fun getMana(): Int {
    return player.persistentDataContainer.getOrDefault(
      NamespacedKey.fromString("smcore:player.mana.int")!!,
      PersistentDataType.INTEGER,
      0
    )
  }

  fun setCurrentMana(mana: Int) {
    player.persistentDataContainer.set(
      NamespacedKey.fromString("smcore:player.current.mana.int")!!,
      PersistentDataType.INTEGER,
      mana
    )
  }

  fun getCurrentMana(): Int {
    return player.persistentDataContainer.getOrDefault(
      NamespacedKey.fromString("smcore:player.current.mana.int")!!,
      PersistentDataType.INTEGER,
      0
    )
  }

  fun setDamage(damage: Int) {
    player.persistentDataContainer.set(
      NamespacedKey.fromString("smcore:player.damage.int")!!,
      PersistentDataType.INTEGER,
      damage
    )
  }

  fun getDamage(): Int {
    return player.persistentDataContainer.getOrDefault(
      NamespacedKey.fromString("smcore:player.damage.int")!!,
      PersistentDataType.INTEGER,
      0
    )
  }

  fun setMaxHealth(maxHealth: Int) {
    player.persistentDataContainer.set(
      NamespacedKey.fromString("smcore:player.maxhealth.int")!!,
      PersistentDataType.INTEGER,
      maxHealth
    )
  }

  @Suppress("unused")
  fun getMaxHealth(): Int {
    return player.persistentDataContainer.getOrDefault(
      NamespacedKey.fromString("smcore:player.maxhealth.int")!!,
      PersistentDataType.INTEGER,
      0
    )
  }

  fun setDefence(def: Int) {
    player.persistentDataContainer.set(
      NamespacedKey.fromString("smcore:player.defence.int")!!,
      PersistentDataType.INTEGER,
      def
    )
  }

  @Suppress("unused")
  fun getDefence(): Int {
    return player.persistentDataContainer.getOrDefault(
      NamespacedKey.fromString("smcore:player.defence.int")!!,
      PersistentDataType.INTEGER,
      0
    )
  }

  fun setBoundNpc(id: Int){
    player.persistentDataContainer.set(
      NamespacedKey.fromString("smentities:player.npc.int")!!,
      PersistentDataType.INTEGER,
      id
    )
  }

  fun getBoundNpc(): Int{
    return player.persistentDataContainer.getOrDefault(
      NamespacedKey.fromString("smentities:player.npc.int")!!,
      PersistentDataType.INTEGER,
      -1
    )
  }
}