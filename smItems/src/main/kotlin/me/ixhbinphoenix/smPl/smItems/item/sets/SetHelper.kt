package me.ixhbinphoenix.smPl.smItems.item.sets

import me.ixhbinphoenix.smPl.smItems.item.EquipmentHandler
import me.ixhbinphoenix.smPl.smItems.item.SetBonus
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class SetHelper {
  companion object {
    /**
     * Gets currently active set of the Player
     * @param armor
     * @param player
     * @return The active Set
     */
    @JvmStatic
    fun getSet(armor: ArrayList<ItemStack?>, player: Player): String {
      var sets = "NONE"
      for (piece in armor) {
        if (piece !is ItemStack) {
          return "NONE"
        } else {
          val handler = EquipmentHandler(piece, player)
          val set = handler.set
          if (set == "NONE") {
            return "NONE"
          } else {
            if (sets != "NONE" && sets != set) {
              return "NONE"
            }
            sets = set
          }
        }
      }
      return sets
    }

    /**
     * Gets how completed the set is
     * @param player
     * @param set
     * @return Amount of pieces equipped
     */
    @JvmStatic
    fun getCompletion(player: Player, set: String): Int {
      var comp = 0
      val armor: ArrayList<ItemStack?> = arrayListOf(
        player.inventory.helmet,
        player.inventory.chestplate,
        player.inventory.leggings,
        player.inventory.boots
      )
      for (piece in armor) {
        if (piece is ItemStack) {
          val handler = EquipmentHandler(piece, player)
          if (handler.set == set) comp++
        }
      }
      return comp
    }
  }
  private val handlers = HashMap<String, BaseSetHandler>()
  val setObjects = HashMap<String, SetBonus>()
  init {
    handlers["PROGRAMMER"] = ProgrammerSet()
    setObjects["PROGRAMMER"] = ProgrammerSet().getBonus()
  }

  /**
   * Trigger recalculate events on equipped sets
   * @param armor
   * @param player
   * @return If a recalculate trigger was executed
   */
  fun calcSet(armor: ArrayList<ItemStack?>, player: Player): Boolean {
    val set = getSet(armor, player)
    return if (handlers.containsKey(set)) {
      handlers[set]?.onRecalc(player)
      true
    } else {
      false
    }
  }
}