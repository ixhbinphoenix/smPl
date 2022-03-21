package me.ixhbinphoenix.smPl.smCore.chat

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.HoverEvent
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor
import org.bukkit.entity.Player


fun getPrefix(player: Player, message: Component): Component {
  // TODO: Load custom badges for player
  val badges = ArrayList<Component>()
  badges.add(
    getRankBadge(player).hoverEvent(HoverEvent.showText(
      getPlayerInfo(player)
    ))
  )
  var prefix = Component.empty()
  for (badge in badges) {
    prefix = prefix.append(badge)
  }
  // TODO: Fetch Rank Color
  val rankColor: TextColor = NamedTextColor.RED
  return prefix
    .append(Component.text(player.name).color(rankColor))
    .append(Component.text(" >> ").color(NamedTextColor.GRAY))
    .append(message.color(NamedTextColor.GRAY))
}

fun getPlayerInfo(player: Player): Component {
  // TODO: Fetch Player level from cache/db
  val playerLevel = 420
  var pinfo = Component.text("Player Info\n").color(NamedTextColor.AQUA)
    .append(createStatText("Name", player.name, NamedTextColor.YELLOW))
    .append(createStatText("Level", playerLevel.toString(), NamedTextColor.YELLOW))
  // TODO: Fetch Player Guild
  val playerGuild: String? = null
  pinfo = if (playerGuild == null) {
    pinfo.append(createStatText("Guild", "None!", NamedTextColor.RED))
  } else {
    pinfo.append(createStatText("Guild", playerGuild, NamedTextColor.YELLOW))
  }
  pinfo = pinfo.append(Component.text("\n"))
  // val infoBadges = getInfoBadges(player)
  val infoBadges = ArrayList<Component>()
  infoBadges.add(
    Component.text("Staff Member").color(NamedTextColor.RED)
      .append(Component.text(" (Lead Admin)").color(NamedTextColor.DARK_GRAY))
  )
  pinfo = pinfo.append(
    createPointList(
      Component.text("Badges").color(NamedTextColor.GREEN),
      infoBadges
    )
  )
  return pinfo
}

fun getRankBadge(player: Player): Component {
  // val rank = getPlayerRank(player)
  val rank = "admin"
  when(rank) {
    "admin" -> {
      return createBadge(NamedTextColor.RED, NamedTextColor.DARK_RED, "\uD83D\uDEE1")
    }
  }
  return Component.empty()
}

fun getInfoBadges(player: Player): Component {
  TODO("Not implemented yet")
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
fun createStatText(statName: String, stat: String, color: TextColor): Component {
  return Component.text("$statName: ").color(NamedTextColor.GRAY)
    .append(Component.text(stat).color(color))
    .append(Component.text("\n"))
}