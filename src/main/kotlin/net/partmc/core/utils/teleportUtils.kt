package net.partmc.core.utils

import net.partmc.core.Core
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitTask
import java.util.*

object TeleportUtils {
    private val pendingTeleports = mutableMapOf<UUID, BukkitTask>()
    private val initialLocations = mutableMapOf<UUID, Location>()

    fun startTeleport(player: Player, location: Location, waitTime: Int, type: String) {
        if (waitTime > 0) {
            player.sendMessage(
                TranslationUtils.getPrefixedTranslation(
                    "$type.waitMessage",
                    mapOf("\$WAIT_TIME" to waitTime.toString())
                )
            )
        } else {
            player.sendMessage(TranslationUtils.getPrefixedTranslation("$type.teleportWithoutWaiting"))
        }

        initialLocations[player.uniqueId] = player.location.clone()

        val delayTicks = waitTime * 20L
        val task: BukkitTask = Bukkit.getScheduler().runTaskLater(Core.instance, Runnable {
            if (pendingTeleports.containsKey(player.uniqueId)) {
                doTeleportSuccess(player, location, type)
            }
        }, delayTicks)

        pendingTeleports[player.uniqueId] = task
    }

    fun cancelTeleport(player: Player) {
        pendingTeleports[player.uniqueId]?.cancel()
        pendingTeleports.remove(player.uniqueId)
        initialLocations.remove(player.uniqueId)

        player.sendMessage(TranslationUtils.getPrefixedTranslation("teleport.cancelMessage"))
    }

    fun isTeleportPending(player: Player): Boolean {
        return pendingTeleports.containsKey(player.uniqueId)
    }

    fun getInitialLocation(player: Player): Location? {
        return initialLocations[player.uniqueId]
    }

    private fun doTeleportSuccess(player: Player, location: Location, type: String) {
        if (!pendingTeleports.containsKey(player.uniqueId)) return

        player.teleport(location)

        val placeholdersTeleport = mapOf(
            "\$X" to location.blockX.toString(),
            "\$Y" to location.blockY.toString(),
            "\$Z" to location.blockZ.toString()
        )

        player.sendMessage(TranslationUtils.getPrefixedTranslation("$type.teleportedMessage", placeholdersTeleport))

        pendingTeleports.remove(player.uniqueId)
        initialLocations.remove(player.uniqueId)
    }
}