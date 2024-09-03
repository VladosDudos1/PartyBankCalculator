package vlados.dudos.domain.utils

import vlados.dudos.domain.model.DebtPair
import vlados.dudos.domain.model.Event
import vlados.dudos.domain.model.Participant
import vlados.dudos.domain.model.Purchase
import vlados.dudos.domain.utils.ListOperationsSupport.getMaxId

object ModelsTransformUtil {
    fun listParticipantsToString(list: List<Participant>): String {
        var resultString = ""
        for ((index, participant) in list.withIndex()) {
            resultString += participant.name
            if (index + 1 != list.size) resultString += ", "
        }
        return resultString
    }
    fun listParticipantsToStringWithAdditionalDebts(list: List<Participant>, listDP: List<DebtPair>): String {
        var resultString = ""
        for ((index, participant) in list.withIndex()) {
            resultString += participant.name
            if (participant in listDP.map { it.debtor }){
                val debtPair = listDP.first { it.debtor == participant }
                if (debtPair.moneySum != 0.0) resultString += " + ${debtPair.moneySum}"
            }
            if (index + 1 != list.size) resultString += ",  "
        }
        return resultString
    }

    fun createNewEvent(
        listIds: List<Int>,
        name: String,
        listParticipant: List<Participant>,
        owner: Participant
    ): Event {
        return Event(
            getMaxId(listIds),
            name = name,
            listParticipant,
            0,
            owner,
            mutableListOf()
        )
    }

    fun createNewPurchase(
        id: Int,
        buyer: Participant,
        listDebtors: MutableList<Participant>
    ): Purchase {
        return Purchase(
            id = id,
            name = "",
            cost = 0.0,
            buyer = buyer,
            listDebtors = listDebtors
        )
    }
}