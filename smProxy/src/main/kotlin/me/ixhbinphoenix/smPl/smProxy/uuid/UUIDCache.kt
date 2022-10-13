package me.ixhbinphoenix.smPl.smProxy.uuid

import com.velocitypowered.api.proxy.Player
import com.velocitypowered.api.util.UuidUtils
import kotlinx.serialization.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import java.net.URL
import java.util.*

@Serializable
data class UUIDResponse(val name: String, val id: String)

@Serializable
data class NameReponse(
  val name: String,
  val id: String,
  @Polymorphic
  val properties: Any
)

@OptIn(ExperimentalSerializationApi::class)
class UUIDCache {

  private val uuidURL = "https://api.mojang.com/users/profiles/minecraft/%s"
  private val nameURL = "https://sessionserver.mojang.com/session/minecraft/profile/%s"

  private val uuidCache = HashMap<String, UUID>()
  private val nameCache = HashMap<UUID, String>()

  /**
   * Gets UUID from player name if the player exists, either from cache or from the API
   * @param name Name of the player
   * @return UUID of the player
   */
  @Suppress("unused")
  fun getUUIDIfExists(name: String): UUID? {
    return if (uuidCache.containsKey(name.lowercase())) {
      uuidCache[name.lowercase()]!!
    } else {
      fetchUUIDIfExists(name.lowercase())
    }
  }


  private fun fetchUUIDIfExists(name: String): UUID? {
    val connection = URL(String.format(uuidURL, name.lowercase())).openConnection()
    connection.readTimeout = 5000
    return try {
      val uuid = UuidUtils.fromUndashed(Json.decodeFromStream<UUIDResponse>(connection.getInputStream()).id)
      uuidCache[name.lowercase()] = uuid
      nameCache[uuid] = name.lowercase()
      uuid
    } catch (e: SerializationException) {
      null
    }
  }

  /**
   * Gets the player name from a UUID, either from cache or from the API
   * @param uuid The UUID of the player
   * @return The name of the player
   */
  @Suppress("unused")
  fun getName(uuid: UUID): String {
    // If someone has somehow obtained a UUID, we can be pretty sure that the player exists
    return if (nameCache.containsKey(uuid)) {
      nameCache[uuid]!!
    } else {
      fetchName(uuid)
    }
  }

  private fun fetchName(uuid: UUID): String {
    val connection = URL(String.format(nameURL, UuidUtils.toUndashed(uuid))).openConnection()
    connection.readTimeout = 5000
    val nameResponse: List<NameReponse> = Json.decodeFromStream(connection.getInputStream())
    val name = nameResponse.last().name.lowercase()
    uuidCache[name] = uuid
    nameCache[uuid] = name
    return name
  }

  /**
   * Adds connected player to the UUID and Name Cache
   * @param player The connected player
   */
  fun addPlayer(player: Player) {
    val name = player.username.lowercase()
    val uuid = player.uniqueId
    uuidCache[name] = uuid
    nameCache[uuid] = name
  }
}