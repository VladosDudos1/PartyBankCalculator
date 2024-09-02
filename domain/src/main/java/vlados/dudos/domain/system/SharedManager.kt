package vlados.dudos.domain.system

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import vlados.dudos.domain.R
import vlados.dudos.domain.model.Event
import vlados.dudos.domain.model.Participant
import vlados.dudos.domain.utils.MapHolder.getValueSign

class SharedManager(private val baseContext: Context) {
    private val shared: SharedPreferences =
        baseContext.getSharedPreferences(
            "PartyBankCalculatorSharedPreferences",
            Context.MODE_PRIVATE
        )

    fun saveThemePreference(themeMode: Int) {
        shared.edit().putInt("isDarkTheme", themeMode).apply()
        shared.edit().putBoolean("isThemeChanged", true).apply()
    }

    fun saveLanguagePreference(languageCode: String) {
        with(shared.edit()) {
            putString("AppLanguage", languageCode)
            apply()
        }
    }

    fun loadLanguagePreference(): String? {
        return shared.getString("AppLanguage", "ru")
    }

    fun isThemeChanged() : Boolean {
        return shared.getBoolean("isThemeChanged", false)
    }

    fun loadThemePreference() {
        val themeMode = shared.getInt("isDarkTheme", AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        AppCompatDelegate.setDefaultNightMode(themeMode)
    }

    fun saveNewEvent(newEvent: Event) {
        val listEvents = getListEvents()
        listEvents.add(newEvent)
        val saveString = Gson().toJson(listEvents)
        shared.edit().putString("EventList", saveString).apply()
    }

    fun deleteEvent(event: Event) {
        val listEvents = getListEvents()
        listEvents.remove(event)
        val saveString = Gson().toJson(listEvents)
        shared.edit().putString("EventList", saveString).apply()
    }

    fun changeCurrentEvent(event: Event) {
        val listEvents = getListEvents()
        listEvents.forEachIndexed { index, nEvent ->
            if (nEvent.id == event.id) {
                listEvents[index] = event
            }
        }
        val saveString = Gson().toJson(listEvents)
        shared.edit().putString("EventList", saveString).apply()
    }

    fun getListEvents(): MutableList<Event> {
        val listEventType = object : TypeToken<MutableList<Event>>() {}.type
        val listEvent = Gson().fromJson<MutableList<Event>>(
            shared.getString("EventList", mutableListOf<Event>().toString())
                ?: mutableListOf<Event>().toString(),
            listEventType
        )
        return listEvent
    }

    fun getEvent(idEvent: Int): Event {
        return getListEvents().first { it.id == idEvent }
    }

    fun saveFriends(friend: Participant, isDelete: Boolean) {
        val listFriends = getFriendsList()
        if (isDelete) listFriends.remove(listFriends.first { it.id == friend.id })
        else if (!listFriends.map { it.id }.contains(friend.id)) listFriends.add(friend)
        else if (listFriends.map { it.id }.contains(friend.id)){
            val friendEdit = listFriends.first { it.id == friend.id }
            friendEdit.name = friend.name
        }
        val saveString = Gson().toJson(listFriends)
        shared.edit().putString("FriendsList", saveString).apply()
    }

    fun getFriendsList(): MutableList<Participant> {
        val listFriendType = object : TypeToken<MutableList<Participant>>() {}.type
        val listFriend = Gson().fromJson<MutableList<Participant>>(
            shared.getString("FriendsList", mutableListOf<Participant>().toString())
                ?: mutableListOf<Participant>().toString(),
            listFriendType
        )
        return listFriend
    }

    fun isFirstLaunch(): Boolean {
        return shared.getBoolean("isFirstLaunch", true)
    }

    fun endFirstLaunch(ownerName: String) {
        shared.edit().putBoolean("isFirstLaunch", false).apply()
        shared.edit().putString("ownerName", ownerName).apply()
    }

    fun getOwnerName(): String {
        return shared.getString("ownerName", baseContext.getString(R.string.you))
            ?: baseContext.getString(R.string.you)
    }

    fun setBaseValue(valueName: String) {
        shared.edit().putString("value", valueName).apply()
    }

    fun setBaseValue() {
        shared.edit()
            .putString("value", getValueSign(baseContext, baseContext.getString(R.string.Ruble)))
            .apply()
    }

    fun getBaseValue(): String {
        return shared.getString("value", "₽") ?: "₽"
    }
}