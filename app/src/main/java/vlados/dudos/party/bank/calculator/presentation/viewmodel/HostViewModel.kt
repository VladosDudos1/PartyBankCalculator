package vlados.dudos.party.bank.calculator.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import vlados.dudos.domain.model.Event

class HostViewModel : ViewModel() {
    private val currentEvent = MutableLiveData<Event>()
    val selectedItem: LiveData<Event> get() = currentEvent

    fun selectItem(event: Event) {
        currentEvent.value = event
    }
}