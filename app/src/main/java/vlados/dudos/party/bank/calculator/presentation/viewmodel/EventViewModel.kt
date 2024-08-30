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
    val purchaseDeleted: LiveData<Unit> get() = _purchaseDeleted

    fun deletePurchase(purchase: Purchase, context: Context, layoutInflater: LayoutInflater, event: Event) {
        val dialogBinding = DeletePurchaseDialogBinding.inflate(layoutInflater)
        val dialog = Dialog(context, R.style.CustomDialogTheme).apply {
            setCancelable(false)
            setContentView(dialogBinding.root)
            dialogBinding.positiveButton.setOnClickListener {
                event.listPurchases.remove(purchase)
                App.sharedManager.changeCurrentEvent(event)
                _purchaseDeleted.postValue(Unit)
                this.dismiss()
            }
            dialogBinding.negativeButton.setOnClickListener {
                this.dismiss()
            }
        }
        dialog.show()
    }
}