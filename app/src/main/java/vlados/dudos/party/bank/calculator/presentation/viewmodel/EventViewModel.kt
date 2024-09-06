package vlados.dudos.party.bank.calculator.presentation.viewmodel

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import vlados.dudos.domain.model.Event
import vlados.dudos.domain.model.Purchase
import vlados.dudos.party.bank.calculator.R
import vlados.dudos.party.bank.calculator.app.App
import vlados.dudos.party.bank.calculator.databinding.DeletePurchaseDialogBinding

class EventViewModel : ViewModel() {
    private val _purchaseDeleted = MutableLiveData<Unit>()
    private val _sum = MutableLiveData<Int?>(null)
    val purchaseDeleted: LiveData<Unit> get() = _purchaseDeleted
    val sum: LiveData<Int?> get() = _sum

    fun deletePurchase(purchase: Purchase, context: Context, layoutInflater: LayoutInflater, event: Event) {
        val dialogBinding = DeletePurchaseDialogBinding.inflate(layoutInflater)
        val dialog = Dialog(context, R.style.CustomDialogTheme).apply {
            setCancelable(false)
            setContentView(dialogBinding.root)
            dialogBinding.positiveButton.setOnClickListener {
                event.listPurchases.remove(purchase)
                event.sum = event.listPurchases.sumOf { it.cost + it.additionalDebts.sumOf { additional -> additional.moneySum } }
                    .toInt()
                _sum.value = event.sum
                App.sharedManager.changeCurrentEvent(event)
                _sum.value = event.listPurchases.sumOf { it.cost + it.additionalDebts.sumOf { additional -> additional.moneySum } }
                    .toInt()
                _purchaseDeleted.postValue(Unit)
                this.dismiss()
            }
            dialogBinding.negativeButton.setOnClickListener {
                this.dismiss()
            }
        }
        dialog.show()
    }
    fun setupSumLiveData(summ: Int){
        _sum.value = summ
    }
}