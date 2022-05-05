package me.ixhbinphoenix.smPl.smItems.item.abilities

import me.ixhbinphoenix.smPl.smItems.item.abilities.normal.FireBook

class Abilities {
  // Key: Either item string_id, or normal ability string ("FIRE_BOOK")
  private val handlers = HashMap<String, AbilityHandler>()

  init {
    handlers["FIRE_BOOK"] = FireBook()
  }

  fun getHandler(id: String): AbilityHandler? {
    return handlers[id]
  }
}