package vlados.dudos.party.bank.calculator.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import vlados.dudos.domain.model.Participant

class FriendsViewModel : ViewModel() {
    private val _editingFriend = MutableLiveData<Participant>()
    val editingFriend get() = _editingFriend

    fun setEditingFriend(participant: Participant){
        _editingFriend.value = participant
    }
}