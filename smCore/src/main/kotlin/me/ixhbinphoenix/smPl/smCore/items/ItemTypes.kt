package me.ixhbinphoenix.smPl.smCore.items

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor

/**
 * Rarities of Items
 * @param color Associated color
 * @param multiplier XP Multiplier
 */
enum class Rarity(val color: TextColor, val multiplier: Int) {
  COMMON(NamedTextColor.WHITE, 500),
  UNCOMMON(NamedTextColor.GREEN, 750),
  RARE(NamedTextColor.BLUE, 1500),
  EPIC(NamedTextColor.DARK_PURPLE, 2250),
  LEGENDARY(NamedTextColor.RED, 3500),
  MYTHIC(NamedTextColor.AQUA, 5000)
}

/**
 * Category of Equipment
 */
enum class EquipmentCategories {
  WEAPON, ARMOR, ACCESSORY
}

/**
 * Types of Equipment
 * @param category Equipment Category
 * @param melee If the equipment if melee
 */
enum class EquipmentTypes(val category: EquipmentCategories, val melee: Boolean) {
  HELMET(EquipmentCategories.ARMOR, false),
  CHESTPLATE(EquipmentCategories.ARMOR, false),
  LEGGINGS(EquipmentCategories.ARMOR, false),
  BOOTS(EquipmentCategories.ARMOR, false),
  SWORD(EquipmentCategories.WEAPON, true),
  DAGGER(EquipmentCategories.WEAPON, true),
  CROSSBOW(EquipmentCategories.WEAPON, false),
  LONGBOW(EquipmentCategories.WEAPON, false),
  SHORTBOW(EquipmentCategories.WEAPON, false),
  WAND(EquipmentCategories.WEAPON, false),
  BOOK(EquipmentCategories.WEAPON, false)
}

/**
 * Elements
 * @param comp Symbol of the Element
 */
enum class Elements(val comp: Component) {
  WATER(Component.text("\uD83C\uDF0A").color(NamedTextColor.DARK_AQUA)),
  FIRE(Component.text("\uD83D\uDD25").color(TextColor.fromCSSHexString("#FFA500"))),
  EARTH(Component.text("⛏").color(TextColor.fromCSSHexString("#8B4513"))),
  AIR(Component.text("☁").color(NamedTextColor.WHITE)),
  LIGHTNING(Component.text("⚡").color(NamedTextColor.GOLD)),
  VOID(Component.text("✹").color(NamedTextColor.DARK_PURPLE))
}