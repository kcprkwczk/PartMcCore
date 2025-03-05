package net.partmc.core

import net.partmc.core.commands.HelloCommand
import org.bukkit.plugin.java.JavaPlugin

class Core : JavaPlugin() {

    override fun onEnable() {
        getCommand("hello")?.setExecutor(HelloCommand())
        logger.info("PartMC Core plugin has been enabled.")
    }



    override fun onDisable() {
        logger.info("PartMC Core plugin has been disabled.")
    }
}
