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
import vlados.dudos.domain.utils.ListOperationsSupport.getMaxId
import vlados.dudos.domain.utils.ModelsTransformUtil.createNewPurchase
import vlados.dudos.party.bank.calculator.app.App

class HostViewModel : ViewModel() {
    private val currentEvent = MutableLiveData<Event>()
    private val newPurchase = MutableLiveData<Purchase>()
    val selectedItem: LiveData<Event> get() = currentEvent
    private val isEventExist = MutableLiveData(false)

    fun selectItem(event: Event) {
        currentEvent.value = event
    }

    fun deleteParticipantFromEvent(participant: Participant){
        val transition = currentEvent.value!!
        transition.listPurchases.forEach { purchase ->
            val debtorIds = purchase.listDebtors.map { it.id }
            val additionalIds = purchase.additionalDebts.map { it.debtor.id }
            if (participant.id in debtorIds)
                purchase.listDebtors.remove(purchase.listDebtors.first { it.id == participant.id })
            if (participant.id in additionalIds)
                purchase.additionalDebts.remove(purchase.additionalDebts.first { it.debtor.id == participant.id })
        }
    }
    fun editParticipantInEvent(participant: Participant, name: String){
        val transition = currentEvent.value!!
        transition.listPurchases.forEach {  purchase ->
            if (purchase.buyer.id == participant.id) purchase.buyer.name = name
            purchase.listDebtors.forEach { debtor ->
                if (debtor.id == participant.id) debtor.name = name
            }
            purchase.additionalDebts.forEach { debtPair ->
                if (debtPair.debtor.id == participant.id) debtPair.debtor.name = name
            }
        }
        selectItem(transition)
    }

    fun setEventSum(sum: Int){
        val transition = currentEvent.value!!
        transition.sum = sum
        currentEvent.value = transition
    }

    fun generatePurchase() {
        newPurchase.value = createNewPurchase(
            if (selectedItem.value?.listPurchases!!.isNotEmpty()) getMaxId(selectedItem.value!!.listPurchases.map { it.id }) else 1,
            selectedItem.value!!.owner,
            mutableListOf()
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
                listDebtors = getCurrentPurchase().listDebtors
                additionalDebts = getCurrentPurchase().additionalDebts
            }
        } else {
            val transit = newPurchase.value!!
            transit.apply {
                cost = costNew
                name = nameNew
                listDebtors = getCurrentPurchase().listDebtors
                additionalDebts = getCurrentPurchase().additionalDebts
            }
            newPurchase.value = transit
            val newTransit = selectedItem.value!!
            newTransit.listPurchases.add(getCurrentPurchase())
            selectItem(newTransit)
        }
    }

    fun savePurchase(cost: Double, name: String) {
        addPurchaseToEvent(cost, name)
        currentEvent.value!!.sum =
            currentEvent.value!!.listPurchases.sumOf { it.cost + it.additionalDebts.sumOf { additional -> additional.moneySum } }
                .toInt()
        App.sharedManager.changeCurrentEvent(currentEvent.value!!)
        selectItem(App.sharedManager.getEvent(currentEvent.value!!.id))
    }

    fun getCurrentPurchase(): Purchase = newPurchase.value!!

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

    fun isEventExist() = isEventExist.value!!
    fun setEventExistValue(value: Boolean) {
        isEventExist.value = value
    }

    fun changeListParticipant(isDelete: Boolean, participant: Participant) {
        if (isDelete) {
            newPurchase.value!!.listDebtors.remove(participant)
            removeDebtorFromAdditional(participant)
        } else {
            newPurchase.value!!.listDebtors.add(participant)
            addToAdditionalSpend(participant, 0.0)
        }
    }

    fun setListDebtors(list: List<Participant>) {
        newPurchase.value!!.listDebtors = list.toMutableList()
    }

    fun setListAdditionalSpending(list: List<DebtPair>) {
        newPurchase.value!!.additionalDebts = list.toMutableList()
    }

    fun addToAdditionalSpend(
        participant: Participant,
        price: Double
    ) {
        if (participant !in getCurrentPurchase().additionalDebts.map { it.debtor }) getCurrentPurchase().additionalDebts.add(
            DebtPair(price, participant)
        )
        else {
            getCurrentPurchase().additionalDebts.forEach {
                if (it.debtor == participant) it.moneySum = price
            }
        }
    }

    private fun removeDebtorFromAdditional(participant: Participant) {
        getCurrentPurchase().additionalDebts.removeIf {
            it.debtor == participant
        }
    }

    fun isNewPurchaseFilled(): Boolean = newPurchase.value!!.listDebtors.isNotEmpty()
}