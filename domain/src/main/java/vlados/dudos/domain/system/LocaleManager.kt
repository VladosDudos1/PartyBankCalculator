package vlados.dudos.domain.system

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import java.util.Locale


class LocaleManager(private val baseContext: Context) {
    private val resources: Resources = baseContext.resources
    private val config: Configuration = resources.configuration
    private val locale = Locale(getLanguageCode())

    fun setLocaleCurrentLanguage() {
        Locale.setDefault(locale)
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
        saveLocaleInShared()
    }

    private fun getLanguageCode(): String {
        return when (Locale.getDefault().language) {
            "ru-RU" -> "ru-RU"
            else -> "en-EN"
        }
    }

    private fun saveLocaleInShared() {
        SharedManager(baseContext).setLocale(getLanguageCode())
    }
}