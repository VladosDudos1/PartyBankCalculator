package vlados.dudos.domain.system

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import vlados.dudos.domain.model.Event

class SharedManager(baseContext: Context) {
    private val shared: SharedPreferences =
        baseContext.getSharedPreferences(
            "PartyBankCalculatorSharedPreferences",
            Context.MODE_PRIVATE
        )

    fun setLocale(locale: String) {
        shared.edit().putString("Locale", locale).apply()
    }

    fun getLocale(): String {
        return shared.getString("Locale", "ru-RU") ?: "ru-RU"
    }

    fun saveNewEvent(newEvent: Event) {
        val listEvents = getListEvents()
        listEvents.add(newEvent)
        val saveString = Gson().toJson(listEvents)
        shared.edit().putString("EventList", saveString).apply()
    }

    fun getListEvents(): MutableList<Event> {
        val listEventType = object : TypeToken<MutableList<Event>>() {}.type
        val listEvent = Gson().fromJson<MutableList<Event>>(
            shared.getString("EventList", mutableListOf<Event>().toString()) ?: mutableListOf<Event>().toString(),
            listEventType
        )
        return listEvent
    }
    fun getEvent(idEvent: Int) : Event{
        return getListEvents().first { it.id ==  idEvent}
    }
}