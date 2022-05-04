package me.ixhbinphoenix.smPl.smItems.db

import me.ixhbinphoenix.smPl.smCore.db.PGEnum
import me.ixhbinphoenix.smPl.smCore.db.createEnumIfExists
import me.ixhbinphoenix.smPl.smItems.Rarity
import me.ixhbinphoenix.smPl.smItems.RarityColor
import me.ixhbinphoenix.smPl.smItems.getInstance
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.logging.Level

object ResourceItems : IntIdTable() {
  val string_id = text("string_id").uniqueIndex()
  val display_name = text("display_name")
  val material = customEnumeration("material", "MaterialEnum", { value -> Material.valueOf(value as String) }, { PGEnum("MaterialEnum", it) })
  val rarity = customEnumeration("rarity", "RarityEnum", {value -> Rarity.valueOf(value as String) }, { PGEnum("RarityEnum", it) })
}

class ResourceItem(id: EntityID<Int>) : IntEntity(id) {
  companion object : IntEntityClass<ResourceItem>(ResourceItems)
  var string_id by ResourceItems.string_id
  var display_name by ResourceItems.display_name
  var material by ResourceItems.material
  var rarity by ResourceItems.rarity
}

class ResourceUtils {
  companion object {
    fun setupDB() {
      val db = getInstance().dbConnection

      transaction(db) {
        exec(createEnumIfExists<Material>("materialenum"))
        exec(createEnumIfExists<Rarity>("rarityenum"))

        try {
          SchemaUtils.createMissingTablesAndColumns(ResourceItems)
        } catch (e: ExposedSQLException) {
          getInstance().logger.log(Level.WARNING, "Creating missing Tables/Columns failed! You'll need to do this manually!")
          throw e
        }
      }
    }
  }
  private val db = getInstance().dbConnection
  private val cache = HashMap<String, ItemStack>()

  fun getItem(id: String): ItemStack? {
    if (cache.contains(id)) {
      return cache[id]
    } else {
      val items = transaction(db) {
        ResourceItem.find { ResourceItems.string_id eq id }.toList()
      }
      return if (items.isNotEmpty()) {
        val dbItem = items[0]
        val item = ItemStack(dbItem.material)
        val im = item.itemMeta
        im.persistentDataContainer.set(NamespacedKey.fromString("smitems:resource.type.str")!!, PersistentDataType.STRING, dbItem.string_id)
        im.persistentDataContainer.set(NamespacedKey.fromString("smitems:item.rarity.str")!!, PersistentDataType.STRING, dbItem.rarity.toString())
        im.displayName(Component.text(dbItem.display_name).color(RarityColor.valueOf(dbItem.rarity.toString()).color).decoration(TextDecoration.ITALIC, false))
        val lore = ArrayList<Component>()
        lore.add(Component.text(dbItem.rarity.toString() + " RESOURCE").color(RarityColor.valueOf(dbItem.rarity.toString()).color).decoration(TextDecoration.ITALIC, false))
        im.lore(lore)
        item.itemMeta = im
        cache[id] = item
        item
      } else {
        null
      }
    }
  }

  fun getRecomms(startsWith: String): MutableList<String> {
    return cache.toList().map { it.first }.filter { it.startsWith(startsWith) }.toMutableList()
  }
}
