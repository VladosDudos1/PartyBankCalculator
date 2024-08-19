package vlados.dudos.party.bank.calculator.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import vlados.dudos.domain.model.Event
import vlados.dudos.domain.model.Participant
import vlados.dudos.domain.model.Purchase
import vlados.dudos.domain.utils.ListOperationsSupport.getMaxId

class HostViewModel : ViewModel() {
    private val currentEvent = MutableLiveData<Event>()
    private val newPurchase = MutableLiveData<Purchase>()
    val selectedItem: LiveData<Event> get() = currentEvent

    fun selectItem(event: Event) {
        currentEvent.value = event
    }

    private fun generatePurchase(participant: Participant) {
        newPurchase.value = Purchase(
            id = if (currentEvent.value?.listPurchases!!.isNotEmpty()) getMaxId(currentEvent.value?.listPurchases!!.map { it.id }) else 1,
            name = "",
            cost = 0.0,
            buyer = participant,
            listDebtors = listOf()
        )
    }

    private fun generatePurchase(list: List<Participant>) {
        newPurchase.value = Purchase(
            id = if (currentEvent.value?.listPurchases!!.isNotEmpty()) getMaxId(currentEvent.value?.listPurchases!!.map { it.id }) else 1,
            name = "",
            cost = 0.0,
            buyer = currentEvent.value!!.owner,
            listDebtors = list
        )
    }

    fun addBuyerToPurchase(buyer: Participant) {
        if (newPurchase.value != null)
            newPurchase.value?.buyer = buyer
        else generatePurchase(buyer)
    }

    fun addDebtorToPurchase(listDebtors: List<Participant>) {
        if (newPurchase.value == null) {
            generatePurchase(listDebtors)
        } else {
            newPurchase.value!!.listDebtors = listDebtors
        }
    }
    fun isNewPurchaseFilled() : Boolean = newPurchase.value!!.buyer.id != -1 && newPurchase.value!!.listDebtors.isNotEmpty()
}