package me.ixhbinphoenix.smPl.smProxy.utils

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor

fun getPermanentBanMessage(reason: String, banID: Int): Component {
  return Component.text("You have been permanently banned from the Network!\n").color(NamedTextColor.RED)
    .append(
      Component.text("Reason: ").color(NamedTextColor.RED)
        .append(Component.text("$reason\n").color(NamedTextColor.YELLOW))
    )
    .append(
      Component.text("Ban ID: ").color(NamedTextColor.RED)
        .append(Component.text("#$banID").color(NamedTextColor.YELLOW))
    )
}

fun getTemporaryBanMessage(reason: String, banID: Int, duration: Long): Component {
  return Component.text("You have been temporarily banned from the Network!\n").color(NamedTextColor.RED)
    .append(
      Component.text("Reason: ").color(NamedTextColor.RED)
        .append(Component.text("$reason\n").color(NamedTextColor.YELLOW))
    )
    .append(
      Component.text("Remaining Time: ").color(NamedTextColor.RED)
        .append(Component.text("${TimeUtils.timeString(duration)}\n").color(NamedTextColor.YELLOW))
    )
    .append(
      Component.text("Ban ID: ").color(NamedTextColor.RED)
        .append(Component.text("#$banID").color(NamedTextColor.YELLOW))
    )
}

fun getPermanentMuteMessage(reason: String, muteID: Int): Component {
  return Component.text("You have been permanently muted!\n").color(NamedTextColor.RED)
    .append(
      Component.text("Reason: ").color(NamedTextColor.RED)
        .append(Component.text("$reason\n").color(NamedTextColor.YELLOW))
    )
    .append(
      Component.text("Mute ID: ").color(NamedTextColor.RED)
        .append(Component.text("#$muteID").color(NamedTextColor.YELLOW))
    )
}

fun getTemporaryMuteMessage(reason: String, muteID: Int, duration: Long): Component {
  return Component.text("You have been temporarily muted!\n").color(NamedTextColor.RED)
    .append(
      Component.text("Reason: ").color(NamedTextColor.RED)
        .append(Component.text("$reason\n").color(NamedTextColor.YELLOW))
    )
    .append(
      Component.text("Remaining Time: ").color(NamedTextColor.RED)
        .append(Component.text("${TimeUtils.timeString(duration)}\n").color(NamedTextColor.YELLOW))
    )
    .append(
      Component.text("Mute ID: ").color(NamedTextColor.RED)
        .append(Component.text("#$muteID").color(NamedTextColor.YELLOW))
    )
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