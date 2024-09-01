package vlados.dudos.party.bank.calculator.app

import android.app.Application
import android.content.Context
import vlados.dudos.domain.calculating.CalculateManager
import vlados.dudos.domain.system.LocaleManager
import vlados.dudos.domain.system.SharedManager
import vlados.dudos.domain.system.ThemeManager

class App : Application() {
    companion object{
        lateinit var sharedManager: SharedManager
        lateinit var localeManager: LocaleManager
        lateinit var calculateManager: CalculateManager
        lateinit var themeManager: ThemeManager
        lateinit var appContext: Context
    }

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
        sharedManager = SharedManager(baseContext)
        localeManager = LocaleManager(baseContext)
        themeManager = ThemeManager(baseContext)
        calculateManager = CalculateManager()
    }
}