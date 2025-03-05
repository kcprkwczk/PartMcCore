package net.partmc.core.commands

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class HelloCommand : CommandExecutor {

    override fun onCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>
    ): Boolean {
        if (sender is Player) {
            sender.sendMessage("Hello, ${sender.name}! You are testing the plugin successfully.")
        } else {
            sender.sendMessage("Hello from console! Plugin command is working.")
        }
        return true
    }
}