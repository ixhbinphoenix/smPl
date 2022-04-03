package me.ixhbinphoenix.smPl.smCore.chat

import me.ixhbinphoenix.smPl.smCore.utils.Rank
import me.ixhbinphoenix.smPl.smCore.utils.getPlayerRank
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextReplacementConfig
import net.kyori.adventure.text.event.HoverEvent
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor
import org.bukkit.Bukkit
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.Sound
import org.bukkit.persistence.PersistentDataType
import kotlin.math.floor


fun getPrefix(player: Player, message: Component): Component {
  var msg = message.color(NamedTextColor.GRAY)
  for (onlinePlayer in Bukkit.getOnlinePlayers()) {
    val msgold = msg
    msg = msg.replaceText(TextReplacementConfig.builder().matchLiteral("@${onlinePlayer.name}").replacement(
      Component.text("@${onlinePlayer.name}").color(NamedTextColor.WHITE)
        .hoverEvent(getPlayerInfo(onlinePlayer))
        .append(Component.empty().color(NamedTextColor.GRAY))
    ).build())
    if (
      msgold != msg
      && player.persistentDataContainer.getOrDefault(NamespacedKey.fromString("smcore:chat.ping.toggle.str")!!, PersistentDataType.STRING, "true") == "true"
    ) {
      onlinePlayer.playSound(onlinePlayer.location, Sound.ENTITY_ARROW_HIT_PLAYER, 0.2f, 1f)
    }
  }
  // TODO: Load custom badges for player
  val badges = ArrayList<Component>()
  badges.add(
    getRankBadge(getPlayerRank(player))
  )
  var prefix = Component.empty()
  for (badge in badges) {
    prefix = prefix.append(badge)
  }
  val rankColor = getRankColor(getPlayerRank(player))
  return prefix
    .append(Component.text(player.name).color(rankColor).hoverEvent(HoverEvent.showText(
      getPlayerInfo(player)
    )))
    .append(Component.text(" >> ").color(NamedTextColor.GRAY))
    .append(msg)
}

fun getPlayerInfo(player: Player): Component {
  // TODO: Fetch Player level from cache/db
  val playerLevel = 420
  var pinfo = Component.text("Player Info\n").color(NamedTextColor.AQUA)
    .append(createStatText("Name", player.name, NamedTextColor.YELLOW))
    .append(createStatText("Level", playerLevel.toString(), NamedTextColor.YELLOW))
  val rank = getPlayerRank(player)
  // TODO: Get Actual rank names
  pinfo = pinfo.append(createStatText("Rank", rank.name, getRankColor(rank)))
  // TODO: Fetch Player Guild
  val playerGuild: String? = null
  pinfo = if (playerGuild == null) {
    pinfo.append(createStatText("Guild", "None!", NamedTextColor.RED, false))
  } else {
    pinfo.append(createStatText("Guild", playerGuild, NamedTextColor.YELLOW, false))
  }
  return pinfo
}

fun getDisplayName(player: Player): Component {
  val rank = getPlayerRank(player)
  // TODO: Load custom badges
  val badges = ArrayList<Component>()
  badges.add(getRankBadge(rank))

  var dp = Component.empty()
  for (badge in badges) {
    dp = dp.append(badge)
  }

  return dp
    .append(Component.text(player.name).color(getRankColor(rank)))
}

fun getRankBadge(rank: Rank): Component {
  return when(rank) {
    Rank.ADMIN -> createBadge(TextColor.fromCSSHexString("#21abcd")!!, TextColor.fromCSSHexString("#125F72")!!, "\uD83D\uDEE1").hoverEvent(getRankInfoText(rank))
    Rank.MODERATOR -> createBadge(NamedTextColor.RED, NamedTextColor.DARK_RED, "\uD83D\uDEE1").hoverEvent(getRankInfoText(rank))
    Rank.BUILDER -> createBadge(NamedTextColor.GREEN, NamedTextColor.DARK_GREEN, "\uD83D\uDEE1").hoverEvent(getRankInfoText(rank))
    Rank.SENATOR -> createBadge(TextColor.fromCSSHexString("#efcc00")!!, TextColor.fromCSSHexString("#b89d00")!!, "★").hoverEvent(getRankInfoText(rank))
    Rank.TRUSTED -> createBadge(NamedTextColor.LIGHT_PURPLE, NamedTextColor.DARK_PURPLE, "⨳").hoverEvent(getRankInfoText(rank))
    Rank.PLAYER -> createBadge(NamedTextColor.GRAY, NamedTextColor.DARK_GRAY, "⨳")
  }
}

fun getRankInfoText(rank: Rank): HoverEvent<Component>? {
  return when(rank) {
    Rank.ADMIN -> HoverEvent.showText(createRankInfoText(rank, "Staff Member", "Administrator"))
    Rank.MODERATOR -> HoverEvent.showText(createRankInfoText(rank, "Staff Member", "Moderator"))
    Rank.BUILDER -> HoverEvent.showText(createRankInfoText(rank, "Staff Member", "Builder"))
    Rank.SENATOR -> HoverEvent.showText(createRankInfoText(rank, "Senate Member", "Senator"))
    Rank.TRUSTED -> HoverEvent.showText(createRankInfoText(rank, "Trusted player", "Trusted"))
    Rank.PLAYER -> null
  }
}

fun getRankColor(rank: Rank): TextColor {
  return when(rank) {
    Rank.ADMIN -> TextColor.fromCSSHexString("#21abcd")!!
    Rank.MODERATOR -> NamedTextColor.RED
    Rank.BUILDER -> NamedTextColor.GREEN
    Rank.SENATOR -> TextColor.fromCSSHexString("#efcc00")!!
    Rank.TRUSTED -> NamedTextColor.LIGHT_PURPLE
    Rank.PLAYER -> NamedTextColor.GRAY
  }
}

fun createProgressBar(bars: Int, progress: Double): Component {
  val barPercent: Double = (100 / bars).toDouble()
  var prog = progress
  var barsWritten = bars

  var bar = Component.text("[").color(NamedTextColor.DARK_GRAY)

  while (prog > barPercent) {
    prog -= barPercent
    bar = bar.append(Component.text("|").color(NamedTextColor.GREEN))
    barsWritten -= 1
  }
  while (barsWritten > 0) {
    bar = bar.append(Component.text("|").color(NamedTextColor.GRAY))
    barsWritten -= 1
  }

  return bar.append(Component.text("]").color(NamedTextColor.DARK_GRAY))
}

fun createRankInfoText(rank: Rank, category: String, specific: String): Component {
  return Component.text(category).color(getRankColor(rank))
    .append(Component.text(" ($specific)").color(NamedTextColor.DARK_GRAY))
}

/**
 * Generates a badge component for chat prefix's
 *
 * @param primary the color of the text in the badge
 * @param secondary the color of the brackets around the text
 * @param text the text inside the badge
 */
fun createBadge(primary: TextColor, secondary: TextColor, text: String): Component {
  return Component.text("[").color(secondary)
    .append(Component.text(text).color(primary))
    .append(Component.text("] ").color(secondary))
}

/**
 * Creates a list with bullet points and a title
 *
 * @param title Title of the list
 * @param options List points
 */
fun createPointList(title: Component, options: ArrayList<Component>): Component {
  var ret = title
  for ( comp in options ) {
    ret = ret.append(
      Component.text("\n• ").color(NamedTextColor.GRAY)
        .append(comp)
    )
  }
  return ret
}

/**
 * Creates Component for stat text
 *
 * @param statName Name of the stat
 * @param stat Value of the stat
 * @param color Color of the stat value
 */
fun createStatText(statName: String, stat: String, color: TextColor, newline: Boolean = true): Component {
  var ret = Component.text("$statName: ").color(NamedTextColor.GRAY)
    .append(Component.text(stat).color(color))
  if (newline) ret = ret.append(Component.text("\n"))
  return ret
}