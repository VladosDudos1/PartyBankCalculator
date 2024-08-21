package vlados.dudos.party.bank.calculator.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import vlados.dudos.domain.model.DebtPair
import vlados.dudos.domain.model.Event
import vlados.dudos.domain.model.Participant
import vlados.dudos.domain.model.Purchase
import vlados.dudos.domain.utils.ListOperationsSupport.cleanTransList
import vlados.dudos.domain.utils.ListOperationsSupport.getMaxId
import vlados.dudos.domain.utils.ListOperationsSupport.getTransList
import vlados.dudos.domain.utils.MapHolder.clearMapAdditionalSpend
import vlados.dudos.domain.utils.MapHolder.getMapAdditionalSpending
import vlados.dudos.domain.utils.ModelsTransformUtil.createNewPurchase
import vlados.dudos.party.bank.calculator.app.App

class HostViewModel : ViewModel() {
    private val currentEvent = MutableLiveData<Event>()
    private val newPurchase = MutableLiveData<Purchase>()
    private var predName: String = ""
    private var predCost: Int = 0
    val selectedItem: LiveData<Event> get() = currentEvent

    fun selectItem(event: Event) {
        currentEvent.value = event
    }

    fun generatePurchase() {
        newPurchase.value = createNewPurchase(
            if (currentEvent.value?.listPurchases!!.isNotEmpty()) getMaxId(currentEvent.value!!.listPurchases!!.map { it.id }) else 1,
            currentEvent.value!!.owner,
            listOf()
        )
    }

    fun addBuyerToPurchase(buyer: Participant) {
        newPurchase.value!!.buyer = buyer
    }

    private fun addPurchaseToEvent(cost: Double, name: String) {
        newPurchase.value!!.cost = cost
        newPurchase.value!!.name = name
        newPurchase.value!!.listDebtors = getTransList()
        newPurchase.value!!.additionalDebts = getMapAdditionalSpending()
        currentEvent.value!!.listPurchases.add(newPurchase.value!!)
    }

    fun savePurchase(cost: Double, name: String) {
        addPurchaseToEvent(cost, name)
        currentEvent.value!!.sum =
            currentEvent.value!!.listPurchases.sumOf { it.cost + it.additionalDebts.sumOf { additional -> additional.moneySum } }
                .toInt()
        App.sharedManager.changeCurrentEvent(currentEvent.value!!)
        clearMapAdditionalSpend()
        cleanTransList()
    }

    fun getCurrentPurchase(): Purchase = newPurchase.value!!
    fun isNewPurchaseFilled(): Boolean =
        newPurchase.value!!.buyer.id != -1 && getTransList().isNotEmpty()

    fun getCost(): Int = predCost
    fun setCost(value: Int) {
        predCost = value
    }

    fun getName(): String = predName
    fun setName(name: String) {
        predName = name
    }
}