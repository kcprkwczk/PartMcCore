package net.partmc.core.listeners

import net.partmc.core.utils.TeleportUtils
import net.partmc.core.utils.TranslationUtils.getPrefixedTranslation
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent
import kotlin.math.abs

class MoveCancelListener : Listener {
    private val threshold = 0.3

    @EventHandler
    fun onPlayerMove(event: PlayerMoveEvent) {
        val player = event.player

        if (!TeleportUtils.isTeleportPending(player)) return

        val initialLocation = TeleportUtils.getInitialLocation(player) ?: return

        Bukkit.getLogger().info("[MoveCancelListener] ${player.name} się ruszył!")

        // Jeśli gracz się poruszy więcej niż 0.3 bloku, anulujemy teleportację
        if (abs(initialLocation.x - event.to.x) > threshold ||
            abs(initialLocation.y - event.to.y) > threshold ||
            abs(initialLocation.z - event.to.z) > threshold
        ) {
            Bukkit.getLogger().info("[MoveCancelListener] Teleportacja ${player.name} została anulowana!")
            TeleportUtils.cancelTeleport(player)
        }
    }
}