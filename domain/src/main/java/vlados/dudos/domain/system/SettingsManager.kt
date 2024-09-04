package vlados.dudos.domain.system

import android.app.Activity
import android.content.Context
import vlados.dudos.domain.R
import java.util.Locale

class SettingsManager(private val baseContext: Context) {
    val sharedManager = SharedManager(baseContext)
    private var resources = baseContext.resources
    private var config = resources.configuration
    private var locale: Locale = Locale(sharedManager.loadLanguagePreference() ?: "ru-RU")
    private var languageList = listOf(
        R.string.russian to "ru-RU",
        R.string.english to "en-EN"
    ).toMap()

    fun setLocaleCurrentLanguage() {
        updateLocale(sharedManager.loadLanguagePreference() ?: "en-EN")
    }

    fun setLanguage(languageCode: String, activity: Activity) {
        updateLocale(languageCode, activity)
        sharedManager.saveLanguagePreference(languageCode)
    }

    fun getMapOfLanguages(): Map<Int, String> = languageList

    fun getLanguageName(): Int {
        val country = locale.country.ifEmpty { "RU" }
        return languageList.entries.firstOrNull { it.value == "${locale.language}-$country" }?.key ?: R.string.english
    }

    private fun updateLocale(languageCode: String, activity: Activity) {
        locale = Locale(languageCode)
        Locale.setDefault(locale)
        config.setLocale(locale)
        activity.resources.updateConfiguration(config, activity.resources.displayMetrics)
        activity.recreate()
    }
    private fun updateLocale(languageCode: String) {
        locale = Locale(languageCode)
        Locale.setDefault(locale)
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
    }

    fun saveThemePreference(themeMode: Int) {
        sharedManager.saveThemePreference(themeMode)
        resources.updateConfiguration(config, resources.displayMetrics)
    }

    fun loadThemePreference() {
        sharedManager.loadThemePreference()
        resources.updateConfiguration(config, resources.displayMetrics)
    }
}