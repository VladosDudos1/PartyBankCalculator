package vlados.dudos.domain.calculating

import vlados.dudos.domain.model.DebtPair
import vlados.dudos.domain.model.Event
import vlados.dudos.domain.model.EventResult
import vlados.dudos.domain.model.Participant
import vlados.dudos.domain.model.Purchase
import java.text.DecimalFormat

class CalculateManager() {
    fun calculateDebts(event: Event): List<EventResult> {
        val result = event.participants.map { participant ->
            EventResult(participant, mutableListOf())
        }.toMutableList()

        for (purchase in event.listPurchases) {
            val costPerPerson = getCostForPerson(purchase)
            val buyerResult = result.find { it.participant == purchase.buyer }

            for (debtor in purchase.listDebtors) {
                val debtPair = buyerResult?.listDebts?.find { it.debtor == debtor }
                if (debtPair != null) {
                    debtPair.moneySum += costPerPerson
                } else {
                    buyerResult?.listDebts?.add(DebtPair(costPerPerson, debtor))
                }
            }
        }
        joinDebts(result)
        sortDebts(result)
        return result
    }

    private fun getCostForPerson(purchase: Purchase): Double {
        val decimalFormat = DecimalFormat("#.##")
        return decimalFormat.format((purchase.cost / purchase.listDebtors.size)).toDouble()
    }
    private fun joinDebts(listEventResult: MutableList<EventResult>){
        listEventResult.removeIf { it.listDebts.isEmpty() }
        for (eventResult in listEventResult){
            eventResult.listDebts.removeIf { it.debtor == eventResult.participant }
        }
    }
    private fun sortDebts(listEventResult: List<EventResult>) : List<EventResult> {
        return listEventResult.sortedBy { it.participant.name }
    }
}