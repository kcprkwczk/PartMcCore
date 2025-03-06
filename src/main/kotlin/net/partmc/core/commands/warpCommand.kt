package net.partmc.core.commands

import net.partmc.core.utils.WarpManager
import net.partmc.core.utils.ConfigUtils
import net.partmc.core.utils.TeleportUtils
import net.partmc.core.utils.TranslationUtils.getPrefixedTranslation
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class WarpCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender !is Player) {
            sender.sendMessage("This command is only available to players!")
            return true
        }

        val player = sender

        if (args.isEmpty()) {
            val warps = WarpManager.getAllWarps()
            if (warps.isEmpty()) {
                player.sendMessage(getPrefixedTranslation("warp.noWarpsAvailable"))
            } else {
                val warpsList = warps.joinToString(", ")
                player.sendMessage(getPrefixedTranslation("warp.list", mapOf("\$WARPS" to warpsList)))
            }
            return true
        }

        val warpName = args[0]

        if (!WarpManager.warpExists(warpName)) {
            player.sendMessage(getPrefixedTranslation("warp.notFound", mapOf("\$WARP" to warpName)))
            return true
        }

        val location = WarpManager.getWarp(warpName)!!
        val waitTime = ConfigUtils.getWaitTimeFor(player)

        TeleportUtils.startTeleport(player, location, waitTime, "warp")

        return true
    }
}
