package net.partmc.core.commands

import io.papermc.lib.PaperLib
import net.kyori.adventure.text.Component
import net.partmc.core.Core
import net.partmc.core.utils.ConfigUtils
import net.partmc.core.utils.TranslationUtils.getPrefixedTranslation
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitTask
import java.util.*
import kotlin.random.Random

class RtpCommand : CommandExecutor {

    override fun onCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>
    ): Boolean {
        if (sender !is Player) {
            sender.sendMessage(Component.text("This command is only available to players!"))
            return true
        }

        val player = sender
        val waitTimeSeconds = ConfigUtils.getWaitTimeFor(player)
        val range = ConfigUtils.getRange()

        if (waitTimeSeconds > 0) {
            player.sendMessage(
                getPrefixedTranslation(
                    "rtp.waitMessage",
                    mapOf("\$WAIT_TIME" to waitTimeSeconds.toString())
                )
            )
        } else {
            player.sendMessage(getPrefixedTranslation("rtp.teleportWithoutWaiting"))
        }

        initialLocations[player.uniqueId] = player.location.clone()

        val delayTicks = waitTimeSeconds * 20L
        val task: BukkitTask = Bukkit.getScheduler().runTaskLater(Core.instance, Runnable {
            if (pendingTeleports.containsKey(player.uniqueId)) {
                attemptTeleportAsync(player, range, attemptsLeft = 10)
            }
        }, delayTicks)

        pendingTeleports[player.uniqueId] = task

        return true
    }


    private fun attemptTeleportAsync(player: Player, range: ConfigUtils.Range, attemptsLeft: Int) {
        if (!pendingTeleports.containsKey(player.uniqueId)) return
        if (attemptsLeft <= 0) {
            fallbackTeleport(player, range)
            return
        }

        val randomX = Random.nextInt(range.minX, range.maxX + 1)
        val randomZ = Random.nextInt(range.minZ, range.maxZ + 1)
        val world = player.world

        PaperLib.getChunkAtAsync(world, randomX shr 4, randomZ shr 4).thenAccept { _ ->
            Bukkit.getScheduler().runTask(Core.instance, Runnable {
                if (!pendingTeleports.containsKey(player.uniqueId)) return@Runnable

                val highestY = world.getHighestBlockAt(randomX, randomZ).y

                if (!world.getBlockAt(randomX, highestY, randomZ).isLiquid) {
                    val safeLocation = Location(world, randomX.toDouble(), highestY.toDouble() + 1, randomZ.toDouble())
                    doTeleportSuccess(player, safeLocation)
                } else {
                    attemptTeleportAsync(player, range, attemptsLeft - 1)
                }
            })
        }
    }

    private fun fallbackTeleport(player: Player, range: ConfigUtils.Range) {
        val randomX = Random.nextInt(range.minX, range.maxX + 1)
        val randomZ = Random.nextInt(range.minZ, range.maxZ + 1)
        val world = player.world
        val highestY = world.getHighestBlockAt(randomX, randomZ).y
        val fallbackLocation = Location(world, randomX.toDouble(), highestY.toDouble() + 1, randomZ.toDouble())
        doTeleportSuccess(player, fallbackLocation)
    }


    private fun doTeleportSuccess(player: Player, location: Location) {
        player.teleport(location)

        val placeholdersTeleport = mapOf(
            "\$X" to location.blockX.toString(),
            "\$Y" to location.blockY.toString(),
            "\$Z" to location.blockZ.toString()
        )

        player.sendMessage(getPrefixedTranslation("rtp.teleportedMessage", placeholdersTeleport))

        pendingTeleports.remove(player.uniqueId)
        initialLocations.remove(player.uniqueId)
    }

    companion object {
        val pendingTeleports = mutableMapOf<UUID, BukkitTask>()
        val initialLocations = mutableMapOf<UUID, Location>()
    }
}