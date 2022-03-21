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


enum class ItemTypes{
    WEAPON, ARMOR, ACCESSORY
}

enum class WeaponTypes{
    SWORD, DAGGER, CROSSBOW, LONGBOW, SHORTBOW, WAND, BOOK
}

data class Xp(val points: Double, val levels: Int)