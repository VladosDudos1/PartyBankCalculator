package vlados.dudos.party.bank.calculator.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import vlados.dudos.party.bank.calculator.app.App

class ListEventViewModel(app: Application) : AndroidViewModel(app) {
    val isFirstLaunch = MutableLiveData<Boolean>(App.sharedManager.isFirstLaunch())
    val eventsList = MutableLiveData(App.sharedManager.getListEvents())

    fun updateEventList(){
        eventsList.value = App.sharedManager.getListEvents()
    }
}