package vlados.dudos.party.bank.calculator.presentation.viewmodel

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
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
import vlados.dudos.party.bank.calculator.R
import vlados.dudos.party.bank.calculator.app.App
import vlados.dudos.party.bank.calculator.databinding.DeletePurchaseDialogBinding
import vlados.dudos.party.bank.calculator.databinding.NameInputLayoutBinding

class HostViewModel : ViewModel() {
    private val currentEvent = MutableLiveData<Event>()
    private val newPurchase = MutableLiveData<Purchase>()
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
        val transition = newPurchase.value!!
        transition.buyer = buyer
        newPurchase.value = transition
    }

    private fun addPurchaseToEvent(costNew: Double, nameNew: String) {
        if (currentEvent.value!!.listPurchases.map { it.id }.contains(newPurchase.value!!.id)) {
            currentEvent.value!!.listPurchases.first { it.id == newPurchase.value!!.id }.apply {
                cost = costNew
                name = nameNew
                listDebtors = getTransList()
                additionalDebts = getMapAdditionalSpending()
            }
        } else {
            val transit = newPurchase.value!!
            transit.apply {
                cost = costNew
                name = nameNew
                listDebtors = getTransList()
                additionalDebts = getMapAdditionalSpending()
            }
            newPurchase.value = transit
            val newTransit = currentEvent.value!!
            newTransit.listPurchases.add(newPurchase.value!!)
            currentEvent.value = newTransit
        }
    }

    fun savePurchase(cost: Double, name: String) {
        addPurchaseToEvent(cost, name)
        currentEvent.value!!.sum =
            currentEvent.value!!.listPurchases.sumOf { it.cost + it.additionalDebts.sumOf { additional -> additional.moneySum } }
                .toInt()
        App.sharedManager.changeCurrentEvent(currentEvent.value!!)
        clearMapAdditionalSpend()
        cleanTransList()
        currentEvent.value = App.sharedManager.getEvent(currentEvent.value!!.id)
    }

    fun getCurrentPurchase(): Purchase = newPurchase.value!!
    fun isNewPurchaseFilled(): Boolean =
        getTransList().isNotEmpty()

    fun getCost(): Int = getCurrentPurchase().cost.toInt()
    fun setCost(value: Int) {
        val transition = newPurchase.value!!
        transition.cost = value.toDouble()
        newPurchase.value = transition
    }

    fun getName(): String = getCurrentPurchase().name
    fun setName(name: String) {
        val transition = newPurchase.value!!
        transition.name = name
        newPurchase.value = transition
    }

    fun setNewPurchase(purchase: Purchase) {
        newPurchase.value = purchase
    }
}