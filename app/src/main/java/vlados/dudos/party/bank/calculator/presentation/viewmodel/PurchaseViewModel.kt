package vlados.dudos.party.bank.calculator.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import vlados.dudos.domain.model.Event
import vlados.dudos.domain.model.Participant
import vlados.dudos.domain.model.Purchase
import vlados.dudos.domain.utils.ListOperationsSupport.getMaxId

class PurchaseViewModel : ViewModel() {
    private val seekBarValue = MutableLiveData<Int>()
    val progress: LiveData<Int> get() = seekBarValue

    fun changeSeekProgress(progress: Int){
        seekBarValue.value = progress
    }
}