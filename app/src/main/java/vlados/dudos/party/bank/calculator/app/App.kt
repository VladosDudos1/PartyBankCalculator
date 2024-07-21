package vlados.dudos.party.bank.calculator.app

import android.app.Application
import vlados.dudos.domain.calculating.CalculateManager
import vlados.dudos.domain.system.LocaleManager
import vlados.dudos.domain.system.SharedManager

class App : Application() {
    companion object{
        lateinit var sharedManager: SharedManager
        lateinit var localeManager: LocaleManager
        lateinit var calculateManager: CalculateManager
    }

    override fun onCreate() {
        super.onCreate()
        sharedManager = SharedManager(baseContext)
        localeManager = LocaleManager(baseContext)
        calculateManager = CalculateManager()
    }
}