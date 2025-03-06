package net.partmc.core.listeners

import net.partmc.core.commands.RtpCommand
import net.partmc.core.utils.ConfigUtils
import net.partmc.core.utils.TranslationUtils.getPrefixedTranslation
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

        val task = RtpCommand.pendingTeleports[uuid] ?: return
        val initialLocation = RtpCommand.initialLocations[uuid] ?: return

        val waitTimeSeconds = ConfigUtils.getWaitTimeFor(player)
        if (waitTimeSeconds == 0) {
            return
        }

        if (abs(initialLocation.x - event.to.x) > threshold ||
            abs(initialLocation.y - event.to.y) > threshold ||
            abs(initialLocation.z - event.to.z) > threshold
        ) {
            task.cancel()
            RtpCommand.pendingTeleports.remove(uuid)
            RtpCommand.initialLocations.remove(uuid)

            player.sendMessage(getPrefixedTranslation("rtp.cancelMessage"))
        }
    }
}