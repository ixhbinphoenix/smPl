package me.ixhbinphoenix.smPl.smProxy.utils

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor

fun getPermanentBanMessage(reason: String, banID: Int): Component {
    return Component.text("You have been permanently banned from the Network!\n").color(NamedTextColor.RED)
        .append(Component.text("Reason: ").color(NamedTextColor.RED)
            .append(Component.text("$reason\n").color(NamedTextColor.YELLOW))
        )
        .append(Component.text("Ban ID: ").color(NamedTextColor.RED)
            .append(Component.text("#$banID").color(NamedTextColor.YELLOW))
        )
}

fun getTemporaryBanMessage(reason: String, banID: Int, duration: Long): Component {
    return Component.text("You have been temporarily banned from the Network!\n").color(NamedTextColor.RED)
        .append(Component.text("Reason: ").color(NamedTextColor.RED)
            .append(Component.text("$reason\n").color(NamedTextColor.YELLOW))
        )
        .append(Component.text("Remaining Time: ").color(NamedTextColor.RED)
            .append(Component.text("${TimeUtils.timeString(duration)}\n").color(NamedTextColor.YELLOW))
        )
        .append(Component.text("Ban ID: ").color(NamedTextColor.RED)
            .append(Component.text("#$banID").color(NamedTextColor.YELLOW))
        )
}