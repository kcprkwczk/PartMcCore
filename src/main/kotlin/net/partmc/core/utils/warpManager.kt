package net.partmc.core.utils

import net.partmc.core.Core
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File

object WarpManager {
    private lateinit var warpsFile: File
    private lateinit var warpsConfig: FileConfiguration

    fun loadWarps() {
        warpsFile = File(Core.instance.dataFolder, "warps.yml")
        if (!warpsFile.exists()) {
            warpsFile.createNewFile()
        }
        warpsConfig = YamlConfiguration.loadConfiguration(warpsFile)
    }

    fun saveWarp(name: String, location: Location) {
        warpsConfig.set("warps.$name.world", location.world?.name)
        warpsConfig.set("warps.$name.x", location.x)
        warpsConfig.set("warps.$name.y", location.y)
        warpsConfig.set("warps.$name.z", location.z)
        warpsConfig.set("warps.$name.yaw", location.yaw)
        warpsConfig.set("warps.$name.pitch", location.pitch)
        warpsConfig.save(warpsFile)
    }

    fun getWarp(name: String): Location? {
        val world = Bukkit.getWorld(warpsConfig.getString("warps.$name.world") ?: return null) ?: return null
        val x = warpsConfig.getDouble("warps.$name.x")
        val y = warpsConfig.getDouble("warps.$name.y")
        val z = warpsConfig.getDouble("warps.$name.z")
        val yaw = warpsConfig.getDouble("warps.$name.yaw").toFloat()
        val pitch = warpsConfig.getDouble("warps.$name.pitch").toFloat()
        return Location(world, x, y, z, yaw, pitch)
    }

    fun warpExists(name: String): Boolean {
        return warpsConfig.contains("warps.$name")
    }
    fun getAllWarps(): List<String> {
        return warpsConfig.getConfigurationSection("warps")?.getKeys(false)?.toList() ?: emptyList()
    }
}
