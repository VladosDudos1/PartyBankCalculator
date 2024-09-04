package vlados.dudos.domain.system

import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import vlados.dudos.domain.R
import vlados.dudos.domain.model.enums.ThemeMode
import java.util.Locale

class SettingsManager(private val baseContext: Context) {
    val sharedManager = SharedManager(baseContext)
    val locale: Locale get() = Locale(sharedManager.loadLanguagePreference() ?: "ru-RU")
    val languageList get() = listOf(
        R.string.russian to "ru-RU",
        R.string.english to "en-EN"
    ).toMap()

    fun getCurrentTheme(): ThemeMode {
        val savedTheme = sharedManager.loadThemePreference()
        return ThemeMode.entries.firstOrNull { it.mode == savedTheme } ?: ThemeMode.LIGHT
    }

    fun setCurrentTheme(){
        AppCompatDelegate.setDefaultNightMode(sharedManager.loadThemePreference())
    }

    fun setTheme(themeMode: ThemeMode) {
        sharedManager.saveThemePreference(themeMode.mode)
        AppCompatDelegate.setDefaultNightMode(themeMode.mode)
    }

    fun getCurrentLanguage(): String {
        return sharedManager.loadLanguagePreference() ?: locale.language
    }

    fun getMapOfLanguages(): Map<Int, String> = languageList
}