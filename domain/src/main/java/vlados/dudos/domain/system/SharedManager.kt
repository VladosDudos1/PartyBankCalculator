package vlados.dudos.domain.system

import android.content.Context
import android.content.SharedPreferences

class SharedManager(baseContext: Context) {
    private val shared: SharedPreferences =
        baseContext.getSharedPreferences("PartyBankCalculatorSharedPreferences", Context.MODE_PRIVATE)
    fun setLocale(locale: String){
        shared.edit().putString("Locale", locale).apply()
    }
    fun getLocale() : String {
        return shared.getString("Locale", "ru-RU") ?: "ru-RU"
    }
}