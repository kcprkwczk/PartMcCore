package net.partmc.core.commands

import net.partmc.core.Core
import net.partmc.core.utils.ConfigUtils
import net.partmc.core.utils.TeleportUtils
import net.partmc.core.utils.TranslationUtils.getPrefixedTranslation
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class SpawnCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender !is Player) {
            sender.sendMessage("Ta komenda jest tylko dla graczy!")
            return true
        }

        val player = sender
        val spawnLocation = getSpawnLocation()

        if (spawnLocation == null) {
            player.sendMessage(getPrefixedTranslation("spawn.notSet"))
            return true
        }

        val waitTime = ConfigUtils.getWaitTimeFor(player)

        TeleportUtils.startTeleport(player, spawnLocation, waitTime, "spawn")

        return true
    }

    private fun getSpawnLocation(): Location? {
        val config = Core.instance.config
        val worldName = config.getString("spawn.location.world") ?: return null
        val world = Bukkit.getWorld(worldName) ?: return null

        val x = config.getDouble("spawn.location.x")
        val y = config.getDouble("spawn.location.y")
        val z = config.getDouble("spawn.location.z")
        val yaw = config.getDouble("spawn.location.yaw").toFloat()
        val pitch = config.getDouble("spawn.location.pitch").toFloat()

        return Location(world, x, y, z, yaw, pitch)
    }
}