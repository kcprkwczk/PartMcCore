package net.partmc.core.utils

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import net.partmc.core.Core
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File

object TranslationUtils {
    private var translations: FileConfiguration? = null

    init {
        loadTranslations()
    }

    fun loadTranslations() {
        val file = File(Core.instance.dataFolder, "translations.yml")
        if (!file.exists()) {
            Core.instance.saveResource("translations.yml", false)
        }
        translations = YamlConfiguration.loadConfiguration(file)
    }

    fun getTranslation(key: String, replacements: Map<String, String> = emptyMap()): Component {
        var translationStr = translations?.getString(key) ?: key
        for ((placeholder, value) in replacements) {
            translationStr = translationStr.replace(placeholder, value)
        }
        return LegacyComponentSerializer.legacyAmpersand().deserialize(translationStr)
    }

    fun getPrefixedTranslation(key: String, replacements: Map<String, String> = emptyMap()): Component {
        val prefix = ConfigUtils.getPrefix()
        val message = getTranslation(key, replacements)
        return prefix
            .append(Component.text(" "))
            .append(message)
    }
}