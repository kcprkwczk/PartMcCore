package net.partmc.core

import net.partmc.core.commands.*
import net.partmc.core.listeners.MoveCancelListener
import net.partmc.core.utils.WarpManager
import org.bukkit.plugin.java.JavaPlugin

class Core : JavaPlugin() {


    override fun onEnable() {
        instance = this
        saveDefaultConfig()
        WarpManager.loadWarps()

        getCommand("rtp")?.setExecutor(rtpCommand())
        getCommand("warp")?.setExecutor(WarpCommand())
        getCommand("setwarp")?.setExecutor(SetWarpCommand())
        getCommand("spawn")?.setExecutor(SpawnCommand())
        getCommand("setspawn")?.setExecutor(SetSpawnCommand())
        getCommand("msg")?.setExecutor(MsgCommand())
        getCommand("r")?.setExecutor(ReplyCommand())
        getCommand("home")?.setExecutor(HomeCommand())
        getCommand("sethome")?.setExecutor(SetHomeCommand())

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