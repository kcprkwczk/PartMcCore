package net.partmc.core.commands

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import net.partmc.core.Core
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.World
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
            sender.sendMessage(Component.text("This command is only available to players!", NamedTextColor.RED))
            return true
        }
        val player = sender

        val config = Core.instance.config
        val prefixStr = config.getString("prefix", "&6Part&bMc")!!
        val prefix = LegacyComponentSerializer.legacyAmpersand().deserialize(prefixStr)
        val waitTimeSeconds = config.getInt("wait-time", 3)
        val delayTicks = waitTimeSeconds * 20L

        val minX = config.getInt("range.min-x", -1000)
        val maxX = config.getInt("range.max-x", 1000)
        val minZ = config.getInt("range.min-z", -1000)
        val maxZ = config.getInt("range.max-z", 1000)

        player.sendMessage(
            prefix.append(
                Component.text(
                    " Teleportation will occur in $waitTimeSeconds seconds. Do not move!",
                    NamedTextColor.YELLOW
                )
            )
        )

        initialLocations[player.uniqueId] = player.location.clone()

        val task: BukkitTask = Bukkit.getScheduler().runTaskLater(Core.instance, Runnable {
            if (pendingTeleports.containsKey(player.uniqueId)) {
                val world: World = player.world

                var attempts = 0
                var safeLocation: Location? = null
                while (attempts < 10 && safeLocation == null) {
                    val randomX = Random.nextInt(minX, maxX + 1)
                    val randomZ = Random.nextInt(minZ, maxZ + 1)
                    val highestBlock = world.getHighestBlockAt(randomX, randomZ)
                    if (!world.getBlockAt(randomX, highestBlock.y, randomZ).isLiquid) {
                        safeLocation =
                            Location(world, randomX.toDouble(), highestBlock.y.toDouble() + 1, randomZ.toDouble())
                    }
                    attempts++
                }

                if (safeLocation == null) {
                    val randomX = Random.nextInt(minX, maxX + 1)
                    val randomZ = Random.nextInt(minZ, maxZ + 1)
                    val highestBlock = world.getHighestBlockAt(randomX, randomZ)
                    safeLocation =
                        Location(world, randomX.toDouble(), highestBlock.y.toDouble() + 1, randomZ.toDouble())
                }

                player.teleport(safeLocation)
                player.sendMessage(
                    prefix.append(
                        Component.text(
                            " Teleported to: X=${safeLocation.blockX}, Y=${safeLocation.blockY}, Z=${safeLocation.blockZ}",
                            NamedTextColor.GREEN
                        )
                    )
                )
                pendingTeleports.remove(player.uniqueId)
                initialLocations.remove(player.uniqueId)
            }
        }, delayTicks)

        pendingTeleports[player.uniqueId] = task

        return true
    }

    companion object {
        val pendingTeleports = mutableMapOf<UUID, BukkitTask>()
        val initialLocations = mutableMapOf<UUID, Location>()
    }
}