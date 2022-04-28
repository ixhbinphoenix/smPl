package me.ixhbinphoenix.smPl.smItems.db

import me.ixhbinphoenix.smPl.smItems.Elements
import me.ixhbinphoenix.smPl.smItems.Rarity
import me.ixhbinphoenix.smPl.smItems.Types
import me.ixhbinphoenix.smPl.smItems.getInstance
import org.bukkit.Material
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.postgresql.util.PGobject
import java.util.logging.Level

class PGEnum<T: Enum<T>>(enumTypeName: String, enumValue: T?): PGobject() {
  init {
    value = enumValue?.name
    type = enumTypeName
  }
}

private inline fun <reified T : Enum<T>> getEnumQuery(dbName: String): String {
  val names = enumValues<T>().map { it.name }
  var str = "CREATE TYPE $dbName AS ENUM ("
  for (name in names) {
    str += "'$name'"
    if (names.indexOf(name) != names.size - 1) {
      str += ", "
    }
  }
  str += ");"
  return str
}

private inline fun <reified T: Enum<T>> createEnumIfExists(dbName: String): String {
  return "DO\n" +
          "$$\n" +
          "BEGIN\n" +
          "IF NOT EXISTS (SELECT * FROM pg_type WHERE typname = '$dbName') THEN\n" +
          " ${getEnumQuery<T>(dbName)}\n" +
          "END IF;\n" +
          "END;\n" +
          "$$\n" +
          "LANGUAGE plpgsql;"
}

object EquipmentItems : IntIdTable() {
  val string_id = text("string_id").uniqueIndex()
  val display_name = text("display_name")
  val material = customEnumeration("material", "MaterialEnum", { value -> Material.valueOf(value as String) }, { PGEnum("MaterialEnum", it) })
  val rarity = customEnumeration("rarity", "RarityEnum", {value -> Rarity.valueOf(value as String) }, { PGEnum("RarityEnum", it) })
  val item_type = customEnumeration("item_type", "TypeEnum", { value -> Types.valueOf(value as String) }, { PGEnum("TypeEnum", it) })
  val element = customEnumeration("element", "ElementEnum", { value -> Elements.valueOf(value as String) }, { PGEnum("ElementEnum", it) }).nullable()
  val set = text("set")
  val rgb = integer("rgb").nullable()
  val defence = integer("defence").default(0)
  val max_health = integer("max_health").default(0)
  val damage = integer("damage").default(0)
  val mana = integer("mana").default(0)
}

class EquipmentItem(id: EntityID<Int>): IntEntity(id) {
  companion object : IntEntityClass<EquipmentItem>(EquipmentItems)
  var string_id by EquipmentItems.string_id
  var display_name by EquipmentItems.display_name
  var material: Material by EquipmentItems.material
  var rarity by EquipmentItems.rarity
  var item_type by EquipmentItems.item_type
  var element by EquipmentItems.element
  var set by EquipmentItems.set
  var rgb by EquipmentItems.rgb
  var defence by EquipmentItems.defence
  var max_health by EquipmentItems.max_health
  var damage by EquipmentItems.damage
  var mana by EquipmentItems.mana
}

class EquipmentUtils {
  companion object {
    fun setupDB() {
      val db = getInstance().dbConnection

      transaction (db) {
        exec(createEnumIfExists<Material>("materialenum"))
        exec(createEnumIfExists<Rarity>("rarityenum"))
        exec(createEnumIfExists<Types>("typeenum"))
        exec(createEnumIfExists<Elements>("elementenum"))
        try {
          SchemaUtils.createMissingTablesAndColumns(EquipmentItems)
        } catch (e: ExposedSQLException) {
          getInstance().logger.log(Level.WARNING, "Creating missing Tables/Columns failed! You'll need to do this manually!")
          throw e
        }
      }
    }

    fun getItem(stringID: String): EquipmentItem? {
      val db = getInstance().dbConnection

      return transaction (db) {
        val item = EquipmentItem.find { EquipmentItems.string_id eq stringID }

        try {
          return@transaction item.toList()[0]
        } catch (e: NoSuchElementException) {
          return@transaction null
        }
      }
    }

    fun getItem(ID: Int): EquipmentItem? {
      val db = getInstance().dbConnection

      return transaction (db) {
        val item = EquipmentItem.find { EquipmentItems.id eq ID }

        try {
          return@transaction item.toList()[0]
        } catch (e: NoSuchElementException) {
          return@transaction null
        }
      }
    }

    fun getRecomms(startsWith: String): MutableList<String> {
      val db = getInstance().dbConnection

      return transaction(db) {
        return@transaction EquipmentItem.find { EquipmentItems.string_id like "${startsWith}%" }.map { it.string_id }.toMutableList()
      }
    }
  }
}
