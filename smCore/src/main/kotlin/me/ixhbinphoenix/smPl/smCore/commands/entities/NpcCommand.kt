package me.ixhbinphoenix.smPl.smCore.commands.entities

import com.comphenix.protocol.wrappers.EnumWrappers
import me.ixhbinphoenix.smPl.smCore.Main
import me.ixhbinphoenix.smPl.smCore.commands.BaseCommand
import me.ixhbinphoenix.smPl.smCore.db.removeNPC
import me.ixhbinphoenix.smPl.smCore.entities.Npc
import me.ixhbinphoenix.smPl.smCore.player.PlayerHandler
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.event.HoverEvent
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.util.StringUtil
import java.util.UUID

class NpcCommand : BaseCommand {
    private val settings = hashMapOf(
        "lookingAtPlayer" to listOf("true", "false"),
        "sneaking" to listOf("true", "false"),
        "layers" to listOf("true", "false"),
        "equip" to listOf("head","chest","legs","feet","mainhand","offhand","likeMe")
    )
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if(sender is Player){
            if (args[0] == "add") {
                if (args.size == 4) {
                    val npcName = args[1]
                    val uuid = UUID.fromString(args[2])
                    val lookAtPlayer = args[3] == "true"
                    Npc().addNpc(npcName, uuid, sender.location, lookAtPlayer)
                    return true
                }
                if (args.size == 3) {
                    val npcName = args[1]
                    val uuid = UUID.fromString(args[2])
                    Npc().addNpc(npcName, uuid, sender.location)
                    return true
                }
            }
            else if(args[0] == "tool"){
                if(sender.inventory.itemInMainHand.type == Material.AIR) sender.inventory.setItemInMainHand(Main.tool)
                else sender.inventory.addItem(Main.tool)
                return true
            }
            else if(args[0] == "bind"){
                if(args.size == 2){
                    val id = args[1].toIntOrNull()!!
                    PlayerHandler(sender).setBoundNpc(id)
                    sender.sendMessage(Component.text("Bound npc with id ").color(NamedTextColor.YELLOW)
                        .append(Component.text(id).color(NamedTextColor.GREEN))
                        .append(Component.text(" to you!").color(NamedTextColor.YELLOW))
                    )
                    return true
                }
            }
            else if(args[0] == "unbind"){
                PlayerHandler(sender).setBoundNpc(0)
                sender.sendMessage(Component.text("Unbound npc from you").color(NamedTextColor.YELLOW))
                return true
            }
            else{
                if(!Main.npcPool.npCs.any { npc -> npc.entityId == PlayerHandler(sender).getBoundNpc()}){
                    sender.sendMessage(Component.text("You are currently not bound to any npc or the npc you were bound to was removed").color(NamedTextColor.RED))
                    return true
                }
                val npc = Main.npcPool.getNpc(PlayerHandler(sender).getBoundNpc())
                val npcHandler = Main.handlerPool[npc.get().entityId]!!
                when(args[0]){
                    "set" -> {
                        if (args.size == 3) {
                            if (settings.containsKey(args[1])) {
                                val setting = args[1]
                                if (settings[args[1]]!!.contains(args[2])) {
                                    when (setting) {
                                        "lookingAtPlayer" -> {
                                            npc.get().isLookAtPlayer = args[2] == "true"
                                            return true
                                        }
                                        "sneaking" -> {
                                            npcHandler.setIsSneaking(args[2] == "true")
                                            return true
                                        }
                                        "layers" -> {
                                            npcHandler.setLayersActive(args[2] == "true")
                                            return true
                                        }
                                        "equip" -> {
                                            Bukkit.getServer().consoleSender.sendMessage(sender.inventory.itemInMainHand.type.toString())
                                            when (args[2]) {
                                                "head" -> npcHandler.setEquipment(EnumWrappers.ItemSlot.HEAD, sender.inventory.itemInMainHand.type)
                                                "chest" -> npcHandler.setEquipment(EnumWrappers.ItemSlot.CHEST, sender.inventory.itemInMainHand.type)
                                                "legs" -> npcHandler.setEquipment(EnumWrappers.ItemSlot.LEGS, sender.inventory.itemInMainHand.type)
                                                "feet" -> npcHandler.setEquipment(EnumWrappers.ItemSlot.FEET, sender.inventory.itemInMainHand.type)
                                                "mainhand" -> npcHandler.setEquipment(EnumWrappers.ItemSlot.MAINHAND, sender.inventory.itemInMainHand.type)
                                                "offhand" -> npcHandler.setEquipment(EnumWrappers.ItemSlot.OFFHAND, sender.inventory.itemInMainHand.type)
                                                "likeMe" -> {
                                                    val equip = mutableListOf(
                                                        if(sender.inventory.helmet != null) sender.inventory.helmet else ItemStack(Material.AIR),
                                                        if(sender.inventory.chestplate != null) sender.inventory.chestplate else ItemStack(Material.AIR),
                                                        if(sender.inventory.leggings != null) sender.inventory.leggings else ItemStack(Material.AIR),
                                                        if(sender.inventory.boots != null) sender.inventory.boots else ItemStack(Material.AIR),
                                                        sender.inventory.itemInMainHand,
                                                        sender.inventory.itemInOffHand
                                                    )
                                                    for (item in equip) {
                                                        when (equip.indexOf(item)) {
                                                            0 -> npcHandler.setEquipment(EnumWrappers.ItemSlot.HEAD, item!!.type)
                                                            1 -> npcHandler.setEquipment(EnumWrappers.ItemSlot.CHEST, item!!.type)
                                                            2 -> npcHandler.setEquipment(EnumWrappers.ItemSlot.LEGS, item!!.type)
                                                            3 -> npcHandler.setEquipment(EnumWrappers.ItemSlot.FEET, item!!.type)
                                                            4 -> npcHandler.setEquipment(EnumWrappers.ItemSlot.MAINHAND, item!!.type)
                                                            5 -> npcHandler.setEquipment(EnumWrappers.ItemSlot.OFFHAND, item!!.type)
                                                        }
                                                    }
                                                    return true
                                                }
                                            }
                                            sender.sendMessage(
                                                Component.text("Set item to ").color(NamedTextColor.YELLOW)
                                                    .append(
                                                        sender.inventory.itemInMainHand.displayName()
                                                            .color(NamedTextColor.GREEN)
                                                    )
                                                    .append(Component.text(" at slot ").color(NamedTextColor.YELLOW))
                                                    .append(Component.text(args[2]).color(NamedTextColor.GREEN))
                                            )
                                            return true
                                        }
                                    }
                                }
                            }
                        }
                    }
                    "remove" -> {
                        Main.npcPool.removeNPC(PlayerHandler(sender).getBoundNpc())
                        removeNPC(npc.get().profile.uniqueId)
                        return true
                    }
                    "rotateTo" -> {
                        val player = Bukkit.getServer().getPlayer(args[1])
                        if (player != null) {
                            npcHandler.lookAt(player.location)
                        } else {
                            sender.sendMessage(
                                Component.text("Player ").color(NamedTextColor.RED)
                                    .append(Component.text(args[1]).color(NamedTextColor.GREEN))
                                    .append(Component.text(" is not online").color(NamedTextColor.RED))
                            )
                        }
                        return true
                    }
                    "tp" -> {
                        if(npc.get().isLookAtPlayer){
                            sender.sendMessage(Component.text("Cannot teleport a npc with ").color(NamedTextColor.RED)
                                .append(Component.text("lookingAtPlayer").color(NamedTextColor.GREEN))
                                .hoverEvent(HoverEvent.showText(Component.text("Disable").color(NamedTextColor.RED)))
                                .clickEvent(ClickEvent.clickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/npc set lookingAtPlayer false"))
                                .append(Component.text(" enabled").color(NamedTextColor.RED))
                            )
                            return true
                        }
                        npc.get().teleport()
                            .queueTeleport(sender.location, sender.isOnGround).send()
                        return true
                    }
                }
            }
        }

        return false
    }

    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>
    ): MutableList<String> {
        val firstArgs = listOf("add","set","remove","bind","unbind","tool","tp","rotateTo","name")
        val setArgs = settings
        val addArgs = listOf(listOf("name"), listOf("uuid"), listOf("true","false"))
        var passedArgs = mutableListOf<String>()
        when(args.size){
            1 -> {
                StringUtil.copyPartialMatches(args[0], firstArgs, passedArgs)
            }
            2 -> {
                when(args[0]){
                    "set" -> StringUtil.copyPartialMatches(args[1], setArgs.keys, passedArgs)
                    "add" -> StringUtil.copyPartialMatches(args[1], addArgs[0], passedArgs)
                    "rotateTo" -> passedArgs = Bukkit.getServer().matchPlayer(args[1]).map { p -> p.name }.toMutableList()
                }
            }
            3 -> {
                if(setArgs.contains(args[1])){
                    StringUtil.copyPartialMatches(args[2], setArgs[args[1]]!!, passedArgs)
                }
                else if(args[0] == "add"){
                    StringUtil.copyPartialMatches(args[2], addArgs[1], passedArgs)
                }
            }
            4 -> {
                if(args[0] == "add"){
                    StringUtil.copyPartialMatches(args[3], addArgs[2], passedArgs)
                }
            }

        }
        return passedArgs
    }
}