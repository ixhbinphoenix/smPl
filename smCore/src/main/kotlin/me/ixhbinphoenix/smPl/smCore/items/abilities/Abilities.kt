package me.ixhbinphoenix.smPl.smCore.items.abilities

import me.ixhbinphoenix.smPl.smCore.items.abilities.normal.FireBook
import me.ixhbinphoenix.smPl.smCore.items.abilities.normal.VoidDagger

class Abilities {
  // Key: Either item id or normal ability String
  private val handlers = HashMap<String, AbilityHandler>()

  init {
    handlers["FIRE_BOOK"] = FireBook()
    handlers["VOID_DAGGER"] = VoidDagger()
  }

  fun getHandler(id: String): AbilityHandler? {
    return handlers[id]
  }
}