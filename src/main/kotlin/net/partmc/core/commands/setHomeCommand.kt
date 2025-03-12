package net.partmc.core.commands

import net.partmc.core.Core
import net.partmc.core.utils.TranslationUtils.getPrefixedTranslation
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class SetHomeCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender !is Player) {
            sender.sendMessage("Ta komenda jest tylko dla graczy!")
            return true
        }

        val player = sender
        val location = player.location

        // Zapisujemy lokalizację home gracza do configu
        val config = Core.instance.config
        config.set("homes.${player.uniqueId}.world", location.world.name)
        config.set("homes.${player.uniqueId}.x", location.x)
        config.set("homes.${player.uniqueId}.y", location.y)
        config.set("homes.${player.uniqueId}.z", location.z)
        config.set("homes.${player.uniqueId}.yaw", location.yaw)
        config.set("homes.${player.uniqueId}.pitch", location.pitch)
        Core.instance.saveConfig()

        player.sendMessage(getPrefixedTranslation("home.setSuccess"))
        return true
    }
}
