package me.ixhbinphoenix.smPl.smItems.item.sets

import me.ixhbinphoenix.smPl.smItems.item.ArmorHandler
import net.kyori.adventure.text.Component
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

class SetHelper {
  companion object {
    @JvmStatic
    fun getSet(armor: ArrayList<ItemStack?>): String {
      var items = 0
      var sets = "NONE"
      for (piece in armor) {
        if (piece !is ItemStack) {
          return "NONE"
        } else {
          items++
          val handler = ArmorHandler(piece)
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
  }
  val sets = HashMap<String, BaseSetHandler>()
  init {
    sets["PROGRAMMER"] = ProgrammerSet()
  }

  fun calcSet(armor: ArrayList<ItemStack?>, player: Player): Boolean {
    val set = getSet(armor)
    if (sets.containsKey(set)) {
      sets[set]?.onRecalc(player)
      return true
    } else {
      return false
    }
  }
}