package me.ixhbinphoenix.smPl.smItems.events

import me.ixhbinphoenix.smPl.smItems.Main
import org.bukkit.Bukkit
import org.bukkit.NamespacedKey
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerItemHeldEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.persistence.PersistentDataType

class Events : Listener {
  // Messy piece of code right here, could definetly be simplified
  @EventHandler
  fun onPlayerEquip(event: PlayerItemHeldEvent) {
    event.player.sendMessage("event triggered")
    val olditem = event.player.inventory.getItem(event.previousSlot)
    val item = event.player.inventory.getItem(event.newSlot)
    if (item is ItemStack && item.hasItemMeta()) {
      val im = item.itemMeta as ItemMeta
      val itemtype = im.persistentDataContainer.get(NamespacedKey.fromString("smitems:item.type.str")!!, PersistentDataType.STRING)
      if (itemtype == "WEAPON") {
        var damage = event.player.persistentDataContainer.getOrDefault(NamespacedKey.fromString("smcore:player.damage.int")!!, PersistentDataType.INTEGER, 0)
        var mana = event.player.persistentDataContainer.getOrDefault(NamespacedKey.fromString("smcore:player.mana.int")!!, PersistentDataType.INTEGER, 0)
        if (olditem is ItemStack && olditem.hasItemMeta()) {
          val oim = olditem.itemMeta as ItemMeta
          val oitype = oim.persistentDataContainer.get(NamespacedKey.fromString("smitems:item.type.str")!!, PersistentDataType.STRING)
          if (oitype == "WEAPON") {
            damage -= oim.persistentDataContainer.getOrDefault(
              NamespacedKey.fromString("smitems:weapon.damage.int")!!,
              PersistentDataType.INTEGER,
              0
            )
            mana -= oim.persistentDataContainer.getOrDefault(
              NamespacedKey.fromString("smitems:weapon.mana.int")!!,
              PersistentDataType.INTEGER,
              0
            )
          }
        }
        mana += im.persistentDataContainer.getOrDefault(NamespacedKey.fromString("smitems:weapon.mana.int")!!, PersistentDataType.INTEGER, 0)
        damage += im.persistentDataContainer.getOrDefault(NamespacedKey.fromString("smitems:weapon.damage.int")!!, PersistentDataType.INTEGER, 0)
        event.player.persistentDataContainer.set(NamespacedKey.fromString("smcore:player.damage.int")!!, PersistentDataType.INTEGER, damage)
        event.player.persistentDataContainer.set(NamespacedKey.fromString("smcore:player.mana.int")!!, PersistentDataType.INTEGER, mana)
      }
    } else if (olditem is ItemStack && olditem.hasItemMeta()) {
      val oim = olditem.itemMeta as ItemMeta;
      if (oim.persistentDataContainer.get(NamespacedKey.fromString("smitems:item.type.str")!!, PersistentDataType.STRING) == "WEAPON") {
        var damage = event.player.persistentDataContainer.getOrDefault(NamespacedKey.fromString("smcore:player.mana.int")!!, PersistentDataType.INTEGER, 0)
        var mana = event.player.persistentDataContainer.getOrDefault(NamespacedKey.fromString("smcore:player.damage.int")!!, PersistentDataType.INTEGER, 0)
        damage -= oim.persistentDataContainer.getOrDefault(NamespacedKey.fromString("smitems:weapon.damage.int")!!, PersistentDataType.INTEGER, 0)
        mana -= oim.persistentDataContainer.getOrDefault(NamespacedKey.fromString("smitems:weapon.mana.int")!!, PersistentDataType.INTEGER, 0)
        event.player.persistentDataContainer.set(NamespacedKey.fromString("smcore:player.damage.int")!!, PersistentDataType.INTEGER, damage)
        event.player.persistentDataContainer.set(NamespacedKey.fromString("smcore:player.mana.int")!!, PersistentDataType.INTEGER, mana)
      }
    }
  }
}