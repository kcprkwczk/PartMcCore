package net.partmc.core.commands

import net.partmc.core.utils.WarpManager
import net.partmc.core.utils.TranslationUtils.getPrefixedTranslation
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class SetWarpCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender !is Player) {
            sender.sendMessage("This command is only available to players!")
            return true
        }

        if (!sender.hasPermission("partmc.setwarp")) {
            sender.sendMessage(getPrefixedTranslation("warp.noPermission"))
            return true
        }

        if (args.isEmpty()) {
            sender.sendMessage(getPrefixedTranslation("warp.setUsage"))
            return true
        }

        val warpName = args[0]
        WarpManager.saveWarp(warpName, sender.location)
        sender.sendMessage(getPrefixedTranslation("warp.setSuccess", mapOf("\$WARP" to warpName)))

        return true
    }
}
