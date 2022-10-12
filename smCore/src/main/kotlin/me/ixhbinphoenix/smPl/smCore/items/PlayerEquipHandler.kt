package me.ixhbinphoenix.smPl.smCore.items

import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

open class PlayerEquipHandler(val player: Player) {

  val head: EquipmentHandler? = getHandler(player.inventory.helmet)
  val chestplate: EquipmentHandler? = getHandler(player.inventory.chestplate)
  val leggings: EquipmentHandler? = getHandler(player.inventory.leggings)
  val boots: EquipmentHandler? = getHandler(player.inventory.boots)
  val mainHand: EquipmentHandler? = getHandler(player.inventory.itemInMainHand)
  val offHand: EquipmentHandler? = getHandler(player.inventory.itemInOffHand)

  private fun getHandler(item: ItemStack?): EquipmentHandler? {
    if (item is ItemStack) {
      if (ItemUtils.isEquipment(item)) {
        return EquipmentHandler(item, player)
      }
    }
    return null
  }

  fun grantXP(xp: Int) {
    if (head is EquipmentHandler) head.setXP(head.xp + xp)
    if (chestplate is EquipmentHandler) chestplate.setXP(chestplate.xp + xp)
    if (leggings is EquipmentHandler) leggings.setXP(leggings.xp + xp)
    if (boots is EquipmentHandler) boots.setXP(boots.xp + xp)
    if (mainHand is EquipmentHandler) mainHand.setXP(mainHand.xp + xp)
    if (offHand is EquipmentHandler) offHand.setXP(offHand.xp + xp)
  }
}
