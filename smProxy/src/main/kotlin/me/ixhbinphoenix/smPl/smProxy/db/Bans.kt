package me.ixhbinphoenix.smPl.smProxy.db

import com.velocitypowered.api.proxy.Player
import me.ixhbinphoenix.smPl.smProxy.getInstance
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.javatime.timestamp
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.Instant
import java.util.UUID

object Bans: IntIdTable() {
    val uuid: Column<String> = char("uuid", 36)
    val moderator: Column<String> = char("moderator", 36)
    val permanent = bool("permanent")
    val bannedAt = timestamp("bannedAt")
    val expiry = timestamp("expiry").nullable()
    val reason = text("reason").nullable()
    val active = bool("active").default(true)
}

class Ban(id: EntityID<Int>): IntEntity(id) {
    companion object : IntEntityClass<Ban>(Bans)
    var uuid by Bans.uuid
    var moderator by Bans.moderator
    var permanent by Bans.permanent
    var bannedAt by Bans.bannedAt
    var expiry by Bans.expiry
    var reason by Bans.reason
    var active by Bans.active
}

class BanUtils {
    companion object {
        fun createBan(player: UUID, moderatorPlayer: Player, isPermanent: Boolean, banReason: String?, banExpiry: Instant?): Int {
            val db = getInstance().dbConnection
            val playerUUID = player.toString()
            val moderatorUUID = moderatorPlayer.uniqueId.toString()


            val id = transaction (db) {

                SchemaUtils.create(Bans)

                return@transaction Ban.new {
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

        fun getSeverestBan(bans: List<Ban>): Ban {
            bans.forEach { ban ->
                if (ban.permanent) {
                    return ban
                }
            }
            val highestExpiry = bans.maxOf { it.expiry ?: Instant.MIN }
            val mappedBans = bans.map { it.expiry ?: Instant.MIN }
            return bans[mappedBans.indexOf(highestExpiry)]
        }

        fun getActiveBans(uuid: UUID): List<Ban> {
            val db = getInstance().dbConnection

            return transaction (db) {
                val bans = Ban.find { Bans.uuid eq uuid.toString() }
                val list = ArrayList<Ban>()

                bans.forEach { ban ->
                    if (ban.expiry is Instant) {
                        val expiry = ban.expiry as Instant
                        if (expiry.isBefore(Instant.now()) && ban.active) {
                            ban.active = false
                        } else if (expiry.isAfter(Instant.now()) && ban.active) {
                            list.add(ban)
                        }
                    } else if (ban.active) {
                        list.add(ban)
                    }
                }
                return@transaction list
            }
        }

        fun getBan(banID: Int): Ban? {
            val db = getInstance().dbConnection

            return transaction (db) {
                try {
                    return@transaction Ban.find { Bans.id eq banID }.first()
                } catch (e: NoSuchElementException) {
                    return@transaction null
                }
            }
        }

        fun hasActiveBan(uuid: UUID): Boolean {
            val db = getInstance().dbConnection

            return transaction (db) {
                val bans = Ban.find { Bans.uuid eq uuid.toString() }
                bans.forEach { ban ->
                    if (ban.expiry is Instant) {
                        val expiry = ban.expiry as Instant
                        if (expiry.isBefore(Instant.now()) && ban.active) {
                            Bans.update({ Bans.id eq ban.id}) {
                                it[active] = false
                            }
                        } else if (expiry.isAfter(Instant.now()) && ban.active) {
                            return@transaction true
                        }
                    } else if (ban.active) {
                        return@transaction true
                    }
                }
                return@transaction false
            }
        }

        fun unBan(uuid: UUID) {
            val db = getInstance().dbConnection

            transaction (db) {
                Ban.find { Bans.uuid eq uuid.toString() }.forEach {
                    it.active = false
                }
            }
        }

        fun unBan(banID: Int) {
            val db = getInstance().dbConnection

            transaction (db) {
                Ban.find { Bans.id eq banID }.forEach {
                    it.active = false
                }
            }
        }
    }
}
