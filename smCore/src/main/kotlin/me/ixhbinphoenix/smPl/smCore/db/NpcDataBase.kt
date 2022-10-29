@file:Suppress("PropertyName")

package me.ixhbinphoenix.smPl.smCore.db

import com.comphenix.protocol.wrappers.EnumWrappers
import com.github.juliarn.npc.NPC
import me.ixhbinphoenix.smPl.smCore.Main
import me.ixhbinphoenix.smPl.smCore.entities.Npc
import me.ixhbinphoenix.smPl.smCore.entities.NpcHandler
import me.ixhbinphoenix.smPl.smCore.getInstance
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*
import java.util.logging.Level

object DataBaseNPCs : IntIdTable(){
    val uuid = text("uuid").uniqueIndex()
    val model_uuid = text("model_uuid")
    val display_name = text("display_name")
    val location_x = double("location_x")
    val location_y = double("location_y")
    val location_z = double("location_z")
    val location_world = text("location_world")
    val is_sneaking = bool("is_sneaking")
    val layers_active = bool("layers_active")
    val looking_x = double("looking_x")
    val looking_y = double("looking_y")
    val looking_z = double("looking_z")
    val looking_world = text("looking_world")
    val looking_at_player = bool("looking_at_player")
    val item_mainhand = customEnumeration("item_mainhand", "MaterialEnum", { v -> Material.valueOf(v as String) }, { PGEnum("MaterialEnum", it) })
    val item_offhand = customEnumeration("item_offhand", "MaterialEnum", { v -> Material.valueOf(v as String) }, { PGEnum("MaterialEnum", it) })
    val item_head = customEnumeration("item_head", "MaterialEnum", { v -> Material.valueOf(v as String) }, { PGEnum("MaterialEnum", it) })
    val item_chest = customEnumeration("item_chest", "MaterialEnum", { v -> Material.valueOf(v as String) }, { PGEnum("MaterialEnum", it) })
    val item_legs = customEnumeration("item_legs", "MaterialEnum", { v -> Material.valueOf(v as String) }, { PGEnum("MaterialEnum", it) })
    val item_feet = customEnumeration("item_feet", "MaterialEnum", { v -> Material.valueOf(v as String) }, { PGEnum("MaterialEnum", it) })
}

class DataBaseNPC(id: EntityID<Int>): IntEntity(id) {
    companion object : IntEntityClass<DataBaseNPC>(DataBaseNPCs)
    var uuid by DataBaseNPCs.uuid
    var model_uuid by DataBaseNPCs.model_uuid
    var display_name by DataBaseNPCs.display_name
    var location_x by DataBaseNPCs.location_x
    var location_y by DataBaseNPCs.location_y
    var location_z by DataBaseNPCs.location_z
    var location_world by DataBaseNPCs.location_world
    var is_sneaking by DataBaseNPCs.is_sneaking
    var layers_active by DataBaseNPCs.layers_active
    var looking_x by DataBaseNPCs.looking_x
    var looking_y by DataBaseNPCs.looking_y
    var looking_z by DataBaseNPCs.looking_z
    var looking_world by DataBaseNPCs.looking_world
    var looking_at_player by DataBaseNPCs.looking_at_player
    var item_mainhand: Material by DataBaseNPCs.item_mainhand
    var item_offhand: Material by DataBaseNPCs.item_offhand
    var item_head: Material by DataBaseNPCs.item_head
    var item_chest: Material by DataBaseNPCs.item_chest
    var item_legs: Material by DataBaseNPCs.item_legs
    var item_feet: Material by DataBaseNPCs.item_feet
}

class NpcDataBase {
    companion object{
        fun setUpDB(){
            val db = getInstance().dbConnection
            transaction(db) {
                exec(createEnumIfExists<Material>("materialenum"))
                try {
                    SchemaUtils.createMissingTablesAndColumns(DataBaseNPCs)
                } catch (e: ExposedSQLException) {
                    getInstance().logger.log(Level.WARNING, "Creating missing Tables/Columns failed! You'll need to do this manually!")
                    throw e
                }
            }
        }
    }
}

fun generateNPCs(){
    val db = getInstance().dbConnection
    transaction(db){
        for (dbNPC in DataBaseNPC.all()) {
            val npc = NPC.builder()
                .profile(
                    Npc().createProfile(
                        dbNPC.display_name,
                        UUID.fromString(dbNPC.model_uuid),
                        UUID.fromString(dbNPC.uuid)
                    )
                )
                .location(
                    Location(
                        Bukkit.getWorld(dbNPC.location_world),
                        dbNPC.location_x,
                        dbNPC.location_y,
                        dbNPC.location_z
                    )
                )
                .imitatePlayer(false)
                .lookAtPlayer(dbNPC.looking_at_player)
                .build(Main.npcPool)
            Main.handlerPool[npc.entityId] = NpcHandler(npc)
            val npcHandler = Main.handlerPool[npc.entityId]!!
            npcHandler.modelUUID = UUID.fromString(dbNPC.model_uuid)
            npcHandler.setLayersActive(dbNPC.layers_active)
            npcHandler.setIsSneaking(dbNPC.is_sneaking)
            npcHandler.setEquipment(EnumWrappers.ItemSlot.MAINHAND, dbNPC.item_mainhand)
            npcHandler.setEquipment(EnumWrappers.ItemSlot.OFFHAND, dbNPC.item_offhand)
            npcHandler.setEquipment(EnumWrappers.ItemSlot.HEAD, dbNPC.item_head)
            npcHandler.setEquipment(EnumWrappers.ItemSlot.CHEST, dbNPC.item_chest)
            npcHandler.setEquipment(EnumWrappers.ItemSlot.LEGS, dbNPC.item_legs)
            npcHandler.setEquipment(EnumWrappers.ItemSlot.FEET, dbNPC.item_feet)
            npcHandler.lookAt(
                Location(
                    Bukkit.getWorld(dbNPC.looking_world),
                    dbNPC.looking_x,
                    dbNPC.looking_y,
                    dbNPC.looking_z
                )
            )
        }
    }
}

fun saveNPCs(){
    val db = getInstance().dbConnection
    transaction(db){
        for (npc in Main.npcPool.npCs) {
            if (DataBaseNPC.find { DataBaseNPCs.uuid eq npc.profile.uniqueId.toString() }.empty()) {
                val handler = Main.handlerPool[npc.entityId]!!
                val infos = handler.getInfos()
                DataBaseNPC.new {
                    uuid = npc.profile.toString()
                    model_uuid = infos.modelUUID.toString()
                    display_name = infos.name
                    location_x = infos.location.x
                    location_y = infos.location.y
                    location_z = infos.location.z
                    location_world = infos.location.world.name
                    is_sneaking = infos.isSneaking
                    layers_active = infos.layersActive
                    looking_x = infos.lookingAt.x
                    looking_y = infos.lookingAt.y
                    looking_z = infos.lookingAt.z
                    looking_world = infos.lookingAt.world.name
                    looking_at_player = infos.isLookingAtPlayer
                    item_mainhand = infos.equipment.mainhand
                    item_offhand = infos.equipment.offhand
                    item_head = infos.equipment.head
                    item_chest = infos.equipment.chest
                    item_legs = infos.equipment.legs
                    item_feet = infos.equipment.feet
                }
            }
        }
    }
}

fun removeNPC(uuid: UUID){
    val db = getInstance().dbConnection
    transaction(db) { DataBaseNPC.find{ DataBaseNPCs.uuid eq uuid.toString() }.toList()[0].delete() }
}