@file:Suppress("unused")

package me.ixhbinphoenix.smPl.smItems

enum class Rarity{
    COMMON, UNCOMMON, RARE, EPIC, LEGENDARY, MYTHIC
}
enum class WeaponTypes{
    SWORD, LONGBOW, SHORTBOW, WAND
}
data class Xp(val points: Double, val levels: Int)