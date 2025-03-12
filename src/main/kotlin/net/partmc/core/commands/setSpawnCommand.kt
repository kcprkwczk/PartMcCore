package net.partmc.core.commands

import net.partmc.core.Core
import net.partmc.core.utils.TranslationUtils.getPrefixedTranslation
import org.bukkit.Location
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class SetSpawnCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender !is Player) {
            sender.sendMessage("Ta komenda jest tylko dla graczy!")
            return true
        }

        val player = sender
        if (!player.hasPermission("partmc.admin")) {
            player.sendMessage(getPrefixedTranslation("spawn.noPermission"))
            return true
        }

        val location = player.location
        Core.instance.config.set("spawn.location.world", location.world.name)
        Core.instance.config.set("spawn.location.x", location.x)
        Core.instance.config.set("spawn.location.y", location.y)
        Core.instance.config.set("spawn.location.z", location.z)
        Core.instance.config.set("spawn.location.yaw", location.yaw)
        Core.instance.config.set("spawn.location.pitch", location.pitch)
        Core.instance.saveConfig()

        val placeholders = mapOf(
            "\$X" to location.blockX.toString(),
            "\$Y" to location.blockY.toString(),
            "\$Z" to location.blockZ.toString()
        )
        player.sendMessage(getPrefixedTranslation("spawn.setSuccess", placeholders))

        return true
    }
}