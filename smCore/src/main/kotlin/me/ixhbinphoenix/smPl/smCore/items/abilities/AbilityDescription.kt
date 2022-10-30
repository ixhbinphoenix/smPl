package me.ixhbinphoenix.smPl.smCore.items.abilities

import me.ixhbinphoenix.smPl.smCore.items.Elements
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.ComponentIteratorType
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration

data class AbilityDescription(val name: String, val description: ArrayList<Component>, val manaCost: Int)

fun genAbilityComponent(primary: Boolean, ability: AbilityDescription, element: Elements?): ArrayList<Component> {
  val components = ArrayList<Component>()
  components.add(Component.text(if(primary) "PRIMARY: " else "SECONDARY: ").color(NamedTextColor.GOLD)
    .append(Component.text(ability.name).color(element?.color ?: NamedTextColor.WHITE)).decoration(TextDecoration.ITALIC, false)
  )
  components.addAll(ability.description)
  if (ability.manaCost > 0) {
    components.add(Component.text("Mana Cost: ").color(NamedTextColor.DARK_GRAY)
        .append(Component.text(ability.manaCost).color(NamedTextColor.DARK_AQUA)).decoration(TextDecoration.ITALIC, false)
    )
  }
  return components
}
