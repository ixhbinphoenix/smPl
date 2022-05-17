package me.ixhbinphoenix.smPl.smItems.item

import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

open class PlayerEquipHandler(val player: Player) {
  private val itemUtils = ItemUtils()

  val head: EquipmentHandler? = getHandler(player.inventory.helmet, player)
  val chestplate: EquipmentHandler? = getHandler(player.inventory.chestplate, player)
  val leggings: EquipmentHandler? = getHandler(player.inventory.leggings, player)
  val boots: EquipmentHandler? = getHandler(player.inventory.boots, player)
  val mainHand: EquipmentHandler? = getHandler(player.inventory.itemInMainHand, player)
  val offHand: EquipmentHandler? = getHandler(player.inventory.itemInOffHand, player)

  private fun getHandler(item: ItemStack?, player: Player): EquipmentHandler? {
    if (item is ItemStack) {
      if (itemUtils.isEquipment(item)) {
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
