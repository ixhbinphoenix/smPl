@file:Suppress("unused")

package me.ixhbinphoenix.smPl.smItems

enum class Rarity{
    COMMON, UNCOMMON, RARE, EPIC, LEGENDARY, MYTHIC
}

enum class ItemTypes{
    WEAPON, ARMOR, ACCESSORY
}

enum class WeaponTypes{
    SWORD, DAGGER, CROSSBOW, LONGBOW, SHORTBOW, WAND, BOOK
}

data class Xp(val points: Double, val levels: Int)