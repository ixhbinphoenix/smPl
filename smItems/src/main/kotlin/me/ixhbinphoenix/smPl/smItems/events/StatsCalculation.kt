package me.ixhbinphoenix.smPl.smItems.events

import me.ixhbinphoenix.smPl.smCore.player.PlayerHandler
import me.ixhbinphoenix.smPl.smItems.ItemCategories
import me.ixhbinphoenix.smPl.smItems.item.ArmorHandler
import me.ixhbinphoenix.smPl.smItems.item.DefaultItemHandler
import me.ixhbinphoenix.smPl.smItems.item.WeaponHandler
import me.ixhbinphoenix.smPl.smItems.item.sets.SetHelper
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitRunnable

class StatsCalculation(private val player: Player, private val refreshLore: Boolean = false) : BukkitRunnable() {
    private val setHandler = SetHelper()

    override fun run() {
        val inventory = player.inventory
        val handler = PlayerHandler(player)
        var damage = 0
        var mana = 0
        var maxHealth = 0
        var defence = 0
        handler.reset()
        val Armor: ArrayList<ItemStack?> = arrayListOf(
            inventory.helmet,
            inventory.chestplate,
            inventory.leggings,
            inventory.boots
        )
        val statsItems: ArrayList<ItemStack?> = arrayListOf(
            inventory.itemInMainHand
        )
        statsItems.addAll(Armor)
        for(item: ItemStack? in statsItems){
            if(item != null && item.hasItemMeta()){
                when(DefaultItemHandler(item, player).category){
                    ItemCategories.WEAPON -> {
                        if (item == inventory.itemInMainHand) {
                            val weapon = WeaponHandler(item, player)
                            damage += weapon.damage
                            mana += weapon.mana
                            handler.setDamage(damage)
                            handler.setMana(mana)
                            if (refreshLore) weapon.updateLore()
                        }
                    }
                    ItemCategories.ARMOR -> {
                        if (
                            item == inventory.helmet ||
                            item == inventory.chestplate ||
                            item == inventory.leggings ||
                            item == inventory.boots
                        ) {
                            val armor = ArmorHandler(item, player)
                            maxHealth += armor.maxHealth
                            defence += armor.defence
                            handler.setMaxHealth(maxHealth)
                            handler.setDefence(defence)
                            if (refreshLore) armor.updateLore()
                        }
                    }
                    else -> {}
                }
            }
        }
        setHandler.calcSet(Armor, player)
    }

}