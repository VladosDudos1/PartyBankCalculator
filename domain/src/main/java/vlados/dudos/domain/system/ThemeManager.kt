package vlados.dudos.domain.system

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.content.res.Resources
import androidx.appcompat.app.AppCompatDelegate
import vlados.dudos.domain.R
import java.util.Locale

class ThemeManager(private val baseContext: Context) {
    val sharedManager = SharedManager(baseContext)
    private val resources: Resources = baseContext.resources
    private val config: Configuration = resources.configuration

    fun saveThemePreference(themeMode: Int) {
        sharedManager.saveThemePreference(themeMode)
        resources.updateConfiguration(config, resources.displayMetrics)
    }

    fun loadThemePreference() {
        sharedManager.loadThemePreference()
        resources.updateConfiguration(config, resources.displayMetrics)
    }
}