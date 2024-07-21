package vlados.dudos.domain.system

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import vlados.dudos.domain.R
import vlados.dudos.domain.model.Event
import vlados.dudos.domain.model.Participant

class SharedManager(val baseContext: Context) {
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
        if (isDelete) listFriends.remove(listFriends.first { it.name == friend.name })
        else if (!listFriends.map { it.name }.contains(friend.name)) listFriends.add(friend)
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
}