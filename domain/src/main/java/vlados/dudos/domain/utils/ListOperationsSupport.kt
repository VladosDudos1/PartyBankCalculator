package vlados.dudos.domain.utils

import vlados.dudos.domain.model.DebtPair
import vlados.dudos.domain.model.Participant
import vlados.dudos.domain.system.SharedManager
import vlados.dudos.domain.utils.MapHolder.addToMapAdditionalSpend
import vlados.dudos.domain.utils.MapHolder.getMapAdditionalSpending
import vlados.dudos.domain.utils.MapHolder.removeDebtorFromAdditionalMap

object ListOperationsSupport {
    private var transitList = mutableListOf<Participant>()
    fun getMaxId(list: List<Int>): Int {
        return if (list.isNotEmpty()) list.max() + 1 else 0
    }

    fun changeListParticipant(isDelete: Boolean, participant: Participant) {
        if (isDelete) {
            transitList.remove(participant)
            removeDebtorFromAdditionalMap(participant)
        } else {
            transitList.add(participant)
            addToMapAdditionalSpend(participant, 0.0)
        }
    }

    fun cleanTransList() {
        transitList = mutableListOf()
    }

    fun getTransList() = transitList
    fun setFullTransList(list: List<Participant>) {
        transitList = list.toMutableList()
    }
}