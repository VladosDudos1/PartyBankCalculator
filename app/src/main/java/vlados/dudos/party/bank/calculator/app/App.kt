package vlados.dudos.party.bank.calculator.app

import android.app.Application
import android.content.Context
import vlados.dudos.domain.calculating.CalculateManager
import vlados.dudos.domain.system.SettingsManager
import vlados.dudos.domain.system.SharedManager

class App : Application() {
    companion object{
        lateinit var sharedManager: SharedManager
        lateinit var settingsManager: SettingsManager
        lateinit var calculateManager: CalculateManager
        lateinit var appContext: Context
    }

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
        sharedManager = SharedManager(baseContext)
        settingsManager = SettingsManager(baseContext)
        calculateManager = CalculateManager()
    }
}