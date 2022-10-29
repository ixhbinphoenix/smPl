package me.ixhbinphoenix.smPl.smCore.entities

import com.comphenix.protocol.wrappers.EnumWrappers.ItemSlot
import com.github.juliarn.npc.NPC
import com.github.juliarn.npc.modifier.MetadataModifier
import com.github.juliarn.npc.profile.Profile
import me.ixhbinphoenix.smPl.smCore.Main
import me.ixhbinphoenix.smPl.smCore.getPool
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import java.util.*

data class NpcInfos(
    val name: String,
    val uuid: UUID,
    val modelUUID: UUID,
    val id: Int,
    val location: Location,
    val equipment: Equipment,
    val isSneaking: Boolean,
    val layersActive: Boolean,
    val lookingAt: Location,
    val isLookingAtPlayer: Boolean
)

data class Equipment(
    var head: Material,
    var chest: Material,
    var legs: Material,
    var feet: Material,
    var mainhand: Material,
    var offhand: Material,
)

class Npc {

    fun addNpc(name: String, uuid: UUID, location: Location, lookAtPlayer: Boolean = false){
        val npc: NPC = NPC.builder()
            .profile(createProfile(name, uuid))
            .location(location)
            .imitatePlayer(false)
            .lookAtPlayer(lookAtPlayer)
            .build(getPool())
        Main.handlerPool[npc.entityId] = NpcHandler(npc)
        Main.handlerPool[npc.entityId]!!.modelUUID = uuid
    }

    fun createProfile(npcName: String, modelUUID: UUID, npcUUID: UUID? = null): Profile{
        val profile = Profile(modelUUID)
        profile.complete()
        profile.name = npcName
        profile.uniqueId = npcUUID ?: UUID.randomUUID()
        return profile
    }
}

class NpcHandler(private val npc: NPC){
    private val equipment = mutableMapOf(
        ItemSlot.HEAD to Material.AIR,
        ItemSlot.CHEST to Material.AIR,
        ItemSlot.LEGS to Material.AIR,
        ItemSlot.FEET to Material.AIR,
        ItemSlot.MAINHAND to Material.AIR,
        ItemSlot.OFFHAND to Material.AIR
    )
    private var isSneaking = false
    private var isShowingLayers = false
    private var isLookingAt = npc.location
    var modelUUID = UUID.randomUUID()

    fun setEquipment(slot: ItemSlot, material: Material){
        equipment[slot] = material
        npc.equipment().queue(slot, ItemStack(material)).send()
    }

    fun getEquipment(slot: ItemSlot): Material{
        return equipment[slot]!!
    }

    fun setIsSneaking(sneak: Boolean){
        isSneaking = sneak
        npc.metadata().queue(MetadataModifier.EntityMetadata.SNEAKING, sneak).send()
    }
    fun getIsSneaking(): Boolean{
        return isSneaking
    }

    fun setLayersActive(active: Boolean){
        isShowingLayers = active
        npc.metadata().queue(MetadataModifier.EntityMetadata.SKIN_LAYERS, active).send()
    }

    fun getLayersActive(): Boolean{
        return isShowingLayers
    }

    fun lookAt(location: Location){
        npc.rotation().queueLookAt(location).send()
    }

    fun getPositionLookingAt(): Location{
        return isLookingAt
    }

    fun getInfos(): NpcInfos{
        val objectEquipment = Equipment(
            head = getEquipment(ItemSlot.HEAD),
            chest = getEquipment(ItemSlot.CHEST),
            legs = getEquipment(ItemSlot.LEGS),
            feet = getEquipment(ItemSlot.FEET),
            mainhand = getEquipment(ItemSlot.MAINHAND),
            offhand = getEquipment(ItemSlot.OFFHAND)
        )

        return NpcInfos(
            npc.profile.name,
            npc.profile.uniqueId,
            modelUUID,
            npc.entityId,
            npc.location,
            objectEquipment,
            isSneaking,
            isShowingLayers,
            isLookingAt,
            npc.isLookAtPlayer
        )
    }

}