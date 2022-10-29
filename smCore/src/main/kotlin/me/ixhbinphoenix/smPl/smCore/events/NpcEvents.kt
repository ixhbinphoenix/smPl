package me.ixhbinphoenix.smPl.smCore.events

import com.github.juliarn.npc.event.PlayerNPCInteractEvent
import me.ixhbinphoenix.smPl.smCore.Main
import me.ixhbinphoenix.smPl.smCore.player.PlayerHandler
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.event.HoverEvent
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

class NpcEvents : Listener {
    @EventHandler
    fun onNpcInteract(event: PlayerNPCInteractEvent){
        val player = event.player
        if(player.inventory.itemInMainHand == Main.tool){
            if(PlayerHandler(player).getBoundNpc() != event.npc.entityId) {
                PlayerHandler(player).setBoundNpc(event.npc.entityId)
                player.sendMessage(
                    Component.text("Bound npc with id ").color(NamedTextColor.YELLOW)
                        .append(
                            Component.text(event.npc.entityId).color(NamedTextColor.GREEN)
                                .clickEvent(
                                    ClickEvent.clickEvent(
                                        ClickEvent.Action.COPY_TO_CLIPBOARD,
                                        event.npc.entityId.toString()
                                    )
                                )
                                .hoverEvent(HoverEvent.showText(Component.text("Copy to clipboard")))
                        )
                        .append(Component.text(" to you!").color(NamedTextColor.YELLOW))
                )
            }
        }
    }
}