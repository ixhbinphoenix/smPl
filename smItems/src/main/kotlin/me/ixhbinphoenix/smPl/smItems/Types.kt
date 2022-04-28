package me.ixhbinphoenix.smPl.smItems

import net.kyori.adventure.text.Component
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

enum class Elements(val comp: Component) {
    WATER(Component.text("\uD83C\uDF0A").color(NamedTextColor.DARK_AQUA)),
    FIRE(Component.text("\uD83D\uDD25").color(TextColor.fromCSSHexString("#FFA500"))),
    EARTH(Component.text("⛏").color(TextColor.fromCSSHexString("#8B4513"))),
    AIR(Component.text("☁").color(NamedTextColor.WHITE)),
    LIGHTNING(Component.text("⚡").color(NamedTextColor.GOLD)),
    VOID(Component.text("✹").color(NamedTextColor.DARK_PURPLE))
}