package me.ixhbinphoenix.smPl.smCore.commands.items

import me.ixhbinphoenix.smPl.smCore.commands.BaseCommand
import me.ixhbinphoenix.smPl.smCore.db.EquipmentItem
import me.ixhbinphoenix.smPl.smCore.db.EquipmentUtils
import me.ixhbinphoenix.smPl.smCore.items.ItemUtils
import me.ixhbinphoenix.smPl.smCore.items.sets.SetHelper
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Color
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.meta.LeatherArmorMeta

class giveItemCommand : BaseCommand {
  private val itemUtils = ItemUtils()
  private val setHelper = SetHelper()

  override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
    if (sender is Player) {
      if (args.isNotEmpty()) {
        val dbItem = EquipmentUtils.getItem(args[0])
        if (dbItem is EquipmentItem) {
          val set = setHelper.setObjects[dbItem.set]
          val stats = hashMapOf(
            "damage" to dbItem.damage,
            "mana" to dbItem.mana,
            "max_health" to dbItem.max_health,
            "defence" to dbItem.defence
          )
          val item = ItemUtils.createEquipment(
            dbItem.material,
            dbItem.display_name,
            dbItem.string_id,
            dbItem.rarity,
            dbItem.item_type,
            stats,
            dbItem.element,
            set
          )
          if (dbItem.rgb is Int) {
            val im = item.itemMeta
            (im as LeatherArmorMeta).setColor(Color.fromRGB(dbItem.rgb!!))
            item.itemMeta = im
          }
          itemUtils.giveItem(item, sender)
        } else {
          sender.sendMessage(Component.text("Unknown Item!").color(NamedTextColor.RED))
        }
      }
    }
    return true
  }

  override fun onTabComplete(
    sender: CommandSender,
    command: Command,
    label: String,
    args: Array<out String>
  ): MutableList<String>? {
    if (args.size == 1) {
      return EquipmentUtils.getRecomms(args[0])
    }
    return null
  }

}