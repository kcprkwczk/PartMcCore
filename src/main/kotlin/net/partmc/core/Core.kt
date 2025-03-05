package net.partmc.core

import net.partmc.core.commands.RtpCommand
import net.partmc.core.listeners.MoveCancelListener
import org.bukkit.plugin.java.JavaPlugin

class Core : JavaPlugin() {


    override fun onEnable() {
        instance = this
        saveDefaultConfig()
        getCommand("rtp")?.setExecutor(RtpCommand())
        server.pluginManager.registerEvents(MoveCancelListener(), this)
        logger.info("PartMC Core plugin has been enabled.")
    }


    override fun onDisable() {
        logger.info("PartMC Core plugin has been disabled.")
    }

    companion object {
        lateinit var instance: Core
            private set
    }
}
