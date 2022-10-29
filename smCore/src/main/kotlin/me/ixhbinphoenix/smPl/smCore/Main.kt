package me.ixhbinphoenix.smPl.smCore

import com.github.juliarn.npc.NPCPool
import me.ixhbinphoenix.smPl.smCore.commands.entities.NpcCommand
import me.ixhbinphoenix.smPl.smCore.commands.entities.SpawnEntityCommand
import me.ixhbinphoenix.smPl.smCore.commands.items.giveItemCommand
import me.ixhbinphoenix.smPl.smCore.commands.items.giveResourceCommand
import me.ixhbinphoenix.smPl.smCore.commands.items.metaCommand
import me.ixhbinphoenix.smPl.smCore.commands.items.setItemXPCommand
import me.ixhbinphoenix.smPl.smCore.commands.stats.delstatCommand
import me.ixhbinphoenix.smPl.smCore.commands.stats.pstatsCommand
import me.ixhbinphoenix.smPl.smCore.commands.stats.setstatCommand
import me.ixhbinphoenix.smPl.smCore.db.*
import me.ixhbinphoenix.smPl.smCore.events.Events
import me.ixhbinphoenix.smPl.smCore.entities.NpcHandler
import me.ixhbinphoenix.smPl.smCore.events.NpcEvents
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin
import org.jetbrains.exposed.sql.Database

@Suppress("unused")
class Main : JavaPlugin() {
  val dbConnection: Database

  init {
    instance = this
    config.options().copyDefaults(true)
    saveDefaultConfig()
    dbConnection = Database.connect(
      config.getString("db.jdbcConnectionString")!!,
      driver = "org.postgresql.Driver",
      user = config.getString("db.user")!!,
      password = config.getString("db.password")!!
    )
    EquipmentUtils.setupDB()
    ResourceUtils.setupDB()
    NpcDataBase.setUpDB()
  }

  override fun onEnable() {
    tool = ItemStack(Material.AMETHYST_SHARD)
    val meta = tool.itemMeta
    meta.displayName(Component.text("Binding Tool").color(NamedTextColor.DARK_PURPLE))
    tool.itemMeta = meta
    npcPool = NPCPool.builder(this)
      .spawnDistance(60)
      .actionDistance(30)
      .tabListRemoveTicks(10)
      .build()
    val commands = hashMapOf(
      "pstats" to pstatsCommand(),
      "setstat" to setstatCommand(),
      "delstat" to delstatCommand(),
      "spawnentity" to SpawnEntityCommand(),
      "giveitem" to giveItemCommand(),
      "giveresource" to giveResourceCommand(),
      "meta" to metaCommand(),
      "setitemxp" to setItemXPCommand(),
      "npc" to NpcCommand()
    )
    generateNPCs()
    for (cmd in commands) {
      getCommand(cmd.key)?.setExecutor(cmd.value)
      getCommand(cmd.key)?.tabCompleter = cmd.value
    }
    server.pluginManager.registerEvents(Events(), this)
    server.pluginManager.registerEvents(NpcEvents(), this)
    server.consoleSender.sendMessage(Component.text("smCore enabled").color(NamedTextColor.GREEN))
  }

  override fun onDisable() {
    saveNPCs()
  }

  companion object {
    lateinit var instance: Main
    lateinit var npcPool: NPCPool
    lateinit var tool: ItemStack
    var handlerPool: HashMap<Int, NpcHandler> = hashMapOf()
  }
}

fun getInstance(): Main {
  return Main.instance
}

fun getPool(): NPCPool{
  return Main.npcPool
}