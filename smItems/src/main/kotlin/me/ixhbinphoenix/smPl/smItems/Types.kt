@file:Suppress("unused")

package me.ixhbinphoenix.smPl.smItems

import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor

enum class Rarity{
    COMMON, UNCOMMON, RARE, EPIC, LEGENDARY, MYTHIC
}
enum class RarityColor(val color: TextColor) {
    COMMON(NamedTextColor.WHITE),
    UNCOMMON(NamedTextColor.GREEN),
    RARE(NamedTextColor.BLUE),
    EPIC(NamedTextColor.DARK_PURPLE),
    LEGENDARY(NamedTextColor.RED),
    MYTHIC(NamedTextColor.AQUA)
}

enum class ItemCategories{
    WEAPON, ARMOR, ACCESSORY
}

enum class ArmorTypes {
    HELMET, CHESTPLATE, LEGGINGS, BOOTS
}

enum class WeaponTypes{
    SWORD, DAGGER, CROSSBOW, LONGBOW, SHORTBOW, WAND, BOOK
}

enum class Types(val category: ItemCategories) {
    HELMET(ItemCategories.ARMOR),
    CHESTPLATE(ItemCategories.ARMOR),
    LEGGINGS(ItemCategories.ARMOR),
    BOOTS(ItemCategories.ARMOR),
    SWORD(ItemCategories.WEAPON),
    DAGGER(ItemCategories.WEAPON),
    CROSSBOW(ItemCategories.WEAPON),
    LONGBOW(ItemCategories.WEAPON),
    SHORTBOW(ItemCategories.WEAPON),
    WAND(ItemCategories.WEAPON),
    BOOK(ItemCategories.WEAPON)
}