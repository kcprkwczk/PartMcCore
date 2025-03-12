package net.partmc.core.commands

import net.partmc.core.utils.TranslationUtils.getPrefixedTranslation
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class ReplyCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender !is Player) {
            sender.sendMessage("Ta komenda jest tylko dla graczy!")
            return true
        }

        val lastSender = MsgCommand.lastMessages[sender]
        if (lastSender == null || !lastSender.isOnline) {
            sender.sendMessage(getPrefixedTranslation("reply.noRecentMessage"))
            return true
        }

        if (args.isEmpty()) {
            sender.sendMessage(getPrefixedTranslation("reply.usage"))
            return true
        }

        val message = args.joinToString(" ")
        sender.sendMessage(getPrefixedTranslation("msg.sent", mapOf("PLAYER" to lastSender.name, "MESSAGE" to message)))
        lastSender.sendMessage(getPrefixedTranslation("msg.received", mapOf("PLAYER" to sender.name, "MESSAGE" to message)))

        // Aktualizacja ostatniego rozmówcy
        MsgCommand.lastMessages[sender] = lastSender
        MsgCommand.lastMessages[lastSender] = sender

        return true
    }
}
