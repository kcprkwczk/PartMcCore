package net.partmc.core.commands

import net.kyori.adventure.text.Component
import net.partmc.core.utils.ConfigUtils
import net.partmc.core.utils.TeleportUtils
import org.bukkit.Location
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import kotlin.random.Random

class rtpCommand : CommandExecutor {

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

        TeleportUtils.startTeleport(player, getRandomLocation(player, range), waitTimeSeconds, "rtp")

        return true
    }

    private fun getRandomLocation(player: Player, range: ConfigUtils.Range): Location {
        val world = player.world
        val randomX = Random.nextInt(range.minX, range.maxX + 1)
        val randomZ = Random.nextInt(range.minZ, range.maxZ + 1)
        val highestY = world.getHighestBlockAt(randomX, randomZ).y
        return Location(world, randomX.toDouble(), highestY.toDouble() + 1, randomZ.toDouble())
    }
}
