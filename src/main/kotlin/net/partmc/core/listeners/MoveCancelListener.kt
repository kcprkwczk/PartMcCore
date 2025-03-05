package net.partmc.core.listeners

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import net.partmc.core.Core
import net.partmc.core.commands.RtpCommand
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent
import kotlin.math.abs

class MoveCancelListener : Listener {

    private val threshold = 0.3

    @EventHandler
    fun onPlayerMove(event: PlayerMoveEvent) {
        val player = event.player
        val uuid = player.uniqueId

        if (RtpCommand.pendingTeleports.containsKey(uuid)) {
            val initialLocation = RtpCommand.initialLocations[uuid]
            if (initialLocation != null) {
                if (abs(initialLocation.x - event.to.x) > threshold ||
                    abs(initialLocation.y - event.to.y) > threshold ||
                    abs(initialLocation.z - event.to.z) > threshold
                ) {

                    RtpCommand.pendingTeleports[uuid]?.cancel()
                    RtpCommand.pendingTeleports.remove(uuid)
                    RtpCommand.initialLocations.remove(uuid)

                    val config = Core.instance.config
                    val prefixStr = config.getString("prefix", "&6Part&bMc")!!
                    val prefix = LegacyComponentSerializer.legacyAmpersand().deserialize(prefixStr)

                    player.sendMessage(
                        prefix.append(Component.text(" Teleportation canceled because you moved!", NamedTextColor.RED))
                    )
                }
            }
        }
    }
}