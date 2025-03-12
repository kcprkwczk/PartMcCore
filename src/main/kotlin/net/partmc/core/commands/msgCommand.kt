package net.partmc.core.commands

import net.partmc.core.utils.TranslationUtils.getPrefixedTranslation
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class MsgCommand : CommandExecutor {
    companion object {
        public val lastMessages = mutableMapOf<Player, Player>()
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender !is Player) {
            sender.sendMessage("Ta komenda jest tylko dla graczy!")
            return true
        }

        if (args.size < 2) {
            sender.sendMessage(getPrefixedTranslation("msg.usage"))
            return true
        }

        val target = Bukkit.getPlayer(args[0])
        if (target == null || !target.isOnline) {
            sender.sendMessage(getPrefixedTranslation("msg.playerNotFound", mapOf("PLAYER" to args[0])))
            return true
        }

        val message = args.drop(1).joinToString(" ")
        sender.sendMessage(getPrefixedTranslation("msg.sent", mapOf("PLAYER" to target.name, "MESSAGE" to message)))
        target.sendMessage(getPrefixedTranslation("msg.received", mapOf("PLAYER" to sender.name, "MESSAGE" to message)))

        // Zapisywanie ostatniego rozmówcy
        lastMessages[sender] = target
        lastMessages[target] = sender

        return true
    }
}
