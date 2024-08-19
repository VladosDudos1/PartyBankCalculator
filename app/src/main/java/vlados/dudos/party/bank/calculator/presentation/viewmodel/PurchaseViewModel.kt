package vlados.dudos.party.bank.calculator.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import vlados.dudos.domain.model.Event

class PurchaseViewModel : ViewModel() {
    private val seekBarValue = MutableLiveData<Int>()
    val progress: LiveData<Int> get() = seekBarValue
    private val selectionValue = MutableLiveData(1)
    val selection: LiveData<Int> get() = seekBarValue

    fun changeSeekProgress(progress: Int){
        seekBarValue.value = progress
    }
    fun setSelection(selection: Int?){
        selectionValue.value = selection ?: 1
    }
}