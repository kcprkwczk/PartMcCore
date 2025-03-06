package net.partmc.core

import net.partmc.core.commands.RtpCommand
import net.partmc.core.commands.SetWarpCommand
import net.partmc.core.commands.WarpCommand
import net.partmc.core.listeners.MoveCancelListener
import net.partmc.core.utils.WarpManager
import org.bukkit.plugin.java.JavaPlugin

class Core : JavaPlugin() {


    override fun onEnable() {
        instance = this
        saveDefaultConfig()
        WarpManager.loadWarps()

        getCommand("rtp")?.setExecutor(RtpCommand())
        getCommand("warp")?.setExecutor(WarpCommand())
        getCommand("setwarp")?.setExecutor(SetWarpCommand())

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
