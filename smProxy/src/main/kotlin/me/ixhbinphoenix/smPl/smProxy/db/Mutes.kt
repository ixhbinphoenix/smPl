package me.ixhbinphoenix.smPl.smProxy.db

import com.velocitypowered.api.proxy.Player
import me.ixhbinphoenix.smPl.smProxy.getInstance
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.javatime.timestamp
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import java.time.Instant
import java.util.UUID


object Mutes : IntIdTable() {
  val uuid: Column<String> = char("uuid", 36)
  val moderator: Column<String> = char("moderator", 36)
  val permanent = bool("permanent")
  val bannedAt = timestamp("bannedAt")
  val expiry = timestamp("expiry").nullable()
  val reason = text("reason").nullable()
  val active = bool("active").default(true)
}

class Mute(id: EntityID<Int>): IntEntity(id) {
  companion object : IntEntityClass<Mute>(Mutes)

  var uuid by Mutes.uuid
  var moderator by Mutes.moderator
  var permanent by Mutes.permanent
  var bannedAt by Mutes.bannedAt
  var expiry by Mutes.expiry
  var reason by Mutes.reason
  var active by Mutes.active
}

class MuteUtils {
  companion object {
    fun createMute(player: UUID, moderatorPlayer: Player, isPermanent: Boolean, banReason: String?, banExpiry: Instant?): Int {
      val db = getInstance().dbConnection
      val playerUUID = player.toString()
      val moderatorUUID = moderatorPlayer.uniqueId.toString()

      val id = transaction(db) {

        SchemaUtils.create(Mutes)

        return@transaction Mute.new {
          uuid = playerUUID
          moderator = moderatorUUID
          permanent = isPermanent
          bannedAt = Instant.now()
          reason = banReason
          expiry = banExpiry
          active = true
        }.id.value
      }
      return id
    }

    fun getSeverestMute(mutes: List<Mute>): Mute {
      mutes.forEach { mute ->
        if (mute.permanent) {
          return mute
        }
      }
      val highestExpiry = mutes.maxOf { it.expiry ?: Instant.MIN }
      val mappedMutes = mutes.map { it.expiry ?: Instant.MIN }
      return mutes[mappedMutes.indexOf(highestExpiry)]
    }

    fun getActiveMutes(uuid: UUID): List<Mute> {
      val db = getInstance().dbConnection

      return transaction(db) {
        val mutes = Mute.find { Mutes.uuid eq uuid.toString() }
        val list = ArrayList<Mute>()

        mutes.forEach { mute ->
          if (mute.expiry is Instant) {
            val expiry = mute.expiry as Instant
            if (expiry.isBefore(Instant.now()) && mute.active) {
              mute.active = false
            } else if (expiry.isAfter(Instant.now()) && mute.active) {
              list.add(mute)
            }
          } else if (mute.active) {
            list.add(mute)
          }
        }

        return@transaction list
      }
    }

    fun hasActiveMute(uuid: UUID): Boolean {
      val db = getInstance().dbConnection

      return transaction(db) {
        val mutes = Mute.find { Mutes.uuid eq uuid.toString() }
        mutes.forEach { mute ->
          if (mute.expiry is Instant) {
            val expiry = mute.expiry as Instant
            if (expiry.isBefore(Instant.now()) && mute.active) {
              Mutes.update({ Mutes.id eq mute.id }) {
                it[active] = false
              }
            } else if (expiry.isAfter(Instant.now()) && mute.active) {
              return@transaction true
            }
          } else if (mute.active) {
            return@transaction true
          }
        }
        return@transaction false
      }
    }

    fun getMute(muteID: Int): Mute? {
      val db = getInstance().dbConnection

      return transaction(db) {
        try {
          return@transaction Mute.find { Mutes.id eq muteID }.first()
        } catch (e: NoSuchElementException) {
          return@transaction null
        }
      }
    }

    fun unMute(uuid: UUID) {
      val db = getInstance().dbConnection

      transaction(db) {
        Mute.find { Mutes.uuid eq uuid.toString() }.forEach {
          it.active = false
        }
      }
    }

    fun unMute(muteID: Int) {
      val db = getInstance().dbConnection

      transaction(db) {
        Mute.find { Mutes.id eq muteID }.forEach {
          it.active = false
        }
      }
    }
  }
}
