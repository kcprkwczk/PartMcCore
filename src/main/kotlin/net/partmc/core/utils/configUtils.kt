package net.partmc.core.utils

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import net.partmc.core.Core
import org.bukkit.entity.Player

object ConfigUtils {

    private const val DEFAULT_PREFIX = "&6Part&bMc"

    fun getPrefix(): Component {
        val prefixStr = Core.instance.config.getString("prefix", DEFAULT_PREFIX)!!
        return LegacyComponentSerializer.legacyAmpersand().deserialize(prefixStr)
    }

    fun getWaitTimeFor(player: Player): Int {
        return when {
            player.hasPermission("partmc.rtp.admin") ->
                Core.instance.config.getInt("wait-times.admin", 0)

            player.hasPermission("partmc.rtp.vip") ->
                Core.instance.config.getInt("wait-times.vip", 3)

            else ->
                Core.instance.config.getInt("wait-times.default", 5)
        }
    }

    data class Range(val minX: Int, val maxX: Int, val minZ: Int, val maxZ: Int)

    fun getRange(): Range {
        val minX = Core.instance.config.getInt("range.min-x", -1000)
        val maxX = Core.instance.config.getInt("range.max-x", 1000)
        val minZ = Core.instance.config.getInt("range.min-z", -1000)
        val maxZ = Core.instance.config.getInt("range.max-z", 1000)
        return Range(minX, maxX, minZ, maxZ)
    }
}