package vlados.dudos.party.bank.calculator.presentation.activity.base

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import vlados.dudos.domain.utils.ActionHolder.getActionId
import vlados.dudos.domain.utils.ActionHolder.setActionId
import vlados.dudos.party.bank.calculator.R
import vlados.dudos.party.bank.calculator.app.App
import java.util.Locale

open class BaseActivity : AppCompatActivity() {
    fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSettings()
    }

    open fun navigate(action: Int) {}
    override fun onBackPressed() {
        val actionId = getActionId()
        if (actionId != -1) {
            setActionId(-1)
            navigate(actionId)
        } else super.onBackPressed()
    }

    private fun setSettings() {
        if (App.sharedManager.isThemeChanged()) {
            App.settingsManager.setCurrentTheme()
        }
        setLanguage(App.settingsManager.getCurrentLanguage(), true)
    }
    open fun setLanguage(languageCode: String, isLaunch: Boolean = false) {
        App.sharedManager.saveLanguagePreference(languageCode)
        updateResources(languageCode, isLaunch)
    }
    open fun getLanguageName(): Int {
        val country = App.settingsManager.locale.country.ifEmpty { "RU" }
        return App.settingsManager.languageList.entries.firstOrNull { it.value == "${App.settingsManager.locale.language}-$country" }?.key ?: vlados.dudos.domain.R.string.english
    }
    private fun updateResources(languageCode: String, isLaunch: Boolean) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val config = resources.configuration
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
        if (!isLaunch) this.recreate()
    }
}