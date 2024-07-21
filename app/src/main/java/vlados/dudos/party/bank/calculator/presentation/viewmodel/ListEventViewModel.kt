package vlados.dudos.party.bank.calculator.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import vlados.dudos.party.bank.calculator.app.App

class ListEventViewModel : ViewModel() {
    val isFirstLaunch = MutableLiveData<Boolean>(App.sharedManager.isFirstLaunch())
}