package vlados.dudos.domain.system

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.content.res.Resources
import org.intellij.lang.annotations.Language
import vlados.dudos.domain.R
import java.util.Locale


class LocaleManager(private val baseContext: Context) {
    private val resources: Resources = baseContext.resources
    private val config: Configuration = resources.configuration
    private val locale = Locale(getLanguageCode())
    private val languageList = listOf(baseContext.getString(R.string.russian), baseContext.getString(R.string.english))

    fun setLocaleCurrentLanguage() {
        Locale.setDefault(locale)
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
    }
    fun getLanguageCode(languageName: String) : String {
        val mapOfLanguage = mapOf(
            languageList[0] to "ru-RU",
            languageList[1] to "en-EN"
        )
        return mapOfLanguage.getValue(languageName)
    }




    private fun getLanguageCode(): String {
        return when (Locale.getDefault().language) {
            "ru-RU" -> "ru-RU"
            else -> "en-EN"
        }
    }
}