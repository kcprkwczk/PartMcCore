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

class HomeCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender !is Player) {
            sender.sendMessage("Ta komenda jest tylko dla graczy!")
            return true
        }

        val player = sender
        val homeLocation = getHomeLocation(player)

        if (homeLocation == null) {
            player.sendMessage(getPrefixedTranslation("home.notSet"))
            return true
        }

        val waitTime = ConfigUtils.getWaitTimeFor(player) // Pobieramy czas teleportacji w zależności od rangi
        TeleportUtils.startTeleport(player, homeLocation, waitTime, "home")
        return true
    }

    private fun getHomeLocation(player: Player): Location? {
        val config = Core.instance.config
        val path = "homes.${player.uniqueId}"

        val worldName = config.getString("$path.world") ?: return null
        val world = Bukkit.getWorld(worldName) ?: return null

        val x = config.getDouble("$path.x")
        val y = config.getDouble("$path.y")
        val z = config.getDouble("$path.z")
        val yaw = config.getDouble("$path.yaw").toFloat()
        val pitch = config.getDouble("$path.pitch").toFloat()

        return Location(world, x, y, z, yaw, pitch)
    }
}
