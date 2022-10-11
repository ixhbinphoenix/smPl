package me.ixhbinphoenix.smPl.smItems.events

import me.ixhbinphoenix.smPl.smCore.player.PlayerHandler
import me.ixhbinphoenix.smPl.smItems.EquipmentCategories
import me.ixhbinphoenix.smPl.smItems.item.EquipmentHandler
import me.ixhbinphoenix.smPl.smItems.item.sets.SetHelper
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import org.bukkit.scheduler.BukkitRunnable

class StatsCalculation(private val player: Player, private val refreshLore: Boolean = false) : BukkitRunnable() {
    private val setHandler = SetHelper()

    override fun run() {
        val inventory = player.inventory
        val handler = PlayerHandler(player)
        val stats = HashMap<String, Int>()
        for (stat in getAllStats()) {
            stats[stat.key] = 0
        }
        handler.reset()
        val armor: ArrayList<ItemStack?> = arrayListOf(
            inventory.helmet,
            inventory.chestplate,
            inventory.leggings,
            inventory.boots
        )
        val statsItems: ArrayList<ItemStack?> = arrayListOf(
            inventory.itemInMainHand
        )
        statsItems.addAll(armor)
        for(item: ItemStack? in statsItems){
            if(item != null && item.hasItemMeta()){
                when(item.itemMeta.persistentDataContainer.getOrDefault(NamespacedKey.fromString("smitems:item.type.str")!!, PersistentDataType.STRING, "???")){
                    EquipmentCategories.WEAPON.toString() -> {
                        if (item == inventory.itemInMainHand) {
                            val itemHandler = EquipmentHandler(item, player)
                            for (stat in getAllStats()) {
                                var value = 0
                                if (stats[stat.key] != null) value = stats[stat.key]!!
                                value += itemHandler.stats[stat.key] as Int
                                stats[stat.key] = value
                            }
                            if (refreshLore) itemHandler.updateLore()
                        }
                    }
                    EquipmentCategories.ARMOR.toString() -> {
                        if (
                            item == inventory.helmet ||
                            item == inventory.chestplate ||
                            item == inventory.leggings ||
                            item == inventory.boots
                        ) {
                            val itemHandler = EquipmentHandler(item, player)
                            for (stat in getAllStats()) {
                                var value = 0
                                if (stats[stat.key] != null) value = stats[stat.key]!!
                                value += itemHandler.stats[stat.key] as Int
                                stats[stat.key] = value
                            }
                            if (refreshLore) itemHandler.updateLore()
                        }
                    }
                    else -> {}
                }
            }
        }
        handler.setDamage(stats["damage"]!!)
        handler.setMana(stats["mana"]!!)
        handler.setMaxHealth(stats["max_health"]!!)
        handler.setDefence(stats["defence"]!!)
        setHandler.calcSet(armor, player)
    }

    companion object {
        @JvmStatic
        fun getAllStats(): HashMap<String, String> {
            val map = HashMap<String, String>()
            map["damage"] = "smitems:equipment.damage.int"
            map["mana"] = "smitems:equipment.mana.int"
            map["defence"] = "smitems:equipment.defence.int"
            map["max_health"] = "smitems:equipment.maxhealth.int"
            return map
        }

        @JvmStatic
        fun statNameToDisplayName(stat: String): String {
            val chars = stat.replace('_', ' ').toCharArray()
            chars[0] = chars[0].uppercaseChar()
            for (i in 1..chars.size) {
                if (chars[i - 1] == ' ') chars[i] = chars[i].uppercaseChar()
            }
            return chars.joinToString("")
        }

        @JvmStatic
        fun displayNameToStatName(stat: String): String {
            return stat.lowercase().replace(' ', '_')
        }

        @JvmStatic
        fun statColor(stat: String): TextColor {
            return when (stat) {
                "damage" -> NamedTextColor.RED
                "mana" -> NamedTextColor.AQUA
                "defence" -> NamedTextColor.GREEN
                "max_health" -> NamedTextColor.RED
                else -> NamedTextColor.YELLOW
            }
        }
    }

}