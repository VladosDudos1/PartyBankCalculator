package vlados.dudos.domain.system

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.content.res.Resources
import org.intellij.lang.annotations.Language
import vlados.dudos.domain.R
import java.util.Locale


class LocaleManager(private val baseContext: Context) {
    val sharedManager = SharedManager(baseContext)
    private val resources: Resources = baseContext.resources
    private var config: Configuration = resources.configuration
    private var locale: Locale = Locale(sharedManager.loadLanguagePreference() ?: "ru-RU")
    private var languageList = listOf(
        R.string.russian to "ru-RU",
        R.string.english to "en-EN"
    ).toMap()

    fun setLocaleCurrentLanguage(activity: Activity) {
        updateLocale(sharedManager.loadLanguagePreference() ?: "en-EN", activity)
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
}