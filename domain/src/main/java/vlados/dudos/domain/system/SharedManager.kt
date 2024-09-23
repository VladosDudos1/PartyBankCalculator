package vlados.dudos.domain.system

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.provider.Telephony.Mms.Part
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

    fun isThemeChanged(): Boolean {
        return shared.getBoolean("isThemeChanged", false)
    }

    fun loadThemePreference(): Int {
        val themeMode = shared.getInt("isDarkTheme", AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        return themeMode
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
        val eventEdited = listEvents.first { it.id == event.id }
        eventEdited.apply {
            name = event.name
            participants = event.participants
            listPurchases = event.listPurchases
            owner = event.owner
            sum = event.sum
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
        else if (listFriends.map { it.id }.contains(friend.id)) {
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
        setOwner(name = ownerName)
        changeOwnerName(ownerName)
    }

    fun changeOwnerName(name: String) {
        shared.edit().putString("ownerName", name).apply()
    }

    fun setOwner(name: String) {
        val owner = Gson().toJson(Participant(10000000, name))
        shared.edit().putString("owner", owner).apply()
        changeOwnerName(name)
    }

    fun getOwner(): Participant {
        val ownerType = object : TypeToken<Participant>() {}.type
        val owner = Gson().fromJson<Participant>(
            shared.getString("owner", null) ?: Participant(
                10000000,
                getOwnerName()
            ).toString(), ownerType
        )
        return owner
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