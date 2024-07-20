package vlados.dudos.domain.calculating

import vlados.dudos.domain.model.DebtPair
import vlados.dudos.domain.model.Event
import vlados.dudos.domain.model.EventResult
import vlados.dudos.domain.model.Participant
import vlados.dudos.domain.model.Purchase
import java.text.DecimalFormat

class CalculateManager() {
    fun calculateDebts(event: Event): List<EventResult> {
        var result = event.participants.map { participant ->
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
        result = cleanDebts(result.toMutableList())
        return result
    }

    private fun getCostForPerson(purchase: Purchase): Double {
        val decimalFormat = DecimalFormat("#.##")
        return decimalFormat.format((purchase.cost / purchase.listDebtors.size)).toDouble()
    }

    private fun joinDebts(listEventResult: MutableList<EventResult>) {
        listEventResult.removeIf { it.listDebts.isEmpty() }
        for (eventResult in listEventResult) {
            eventResult.listDebts.removeIf { it.debtor == eventResult.participant }
        }
    }

    private fun cleanDebts(listEventResult: MutableList<EventResult>) : MutableList<EventResult>{
        val debtMap = mutableMapOf<Pair<Participant, Participant>, Double>()
        val consolidatedMap = mutableMapOf<Pair<Participant, Participant>, Double>()
        val consolidatedResults = mutableMapOf<Participant, MutableList<DebtPair>>()
        for (eventResult in listEventResult) {
            for (debtPair in eventResult.listDebts) {
                val key = eventResult.participant to debtPair.debtor
                debtMap[key] = debtMap.getOrDefault(key, 0).toDouble() + debtPair.moneySum
            }
        }
        for ((key, sum) in debtMap) {
            val (participant1, participant2) = key
            val reverseKey = participant2 to participant1
            val reverseSum = debtMap.getOrDefault(reverseKey, 0)

            val netSum = sum - reverseSum.toInt()
            if (netSum > 0) {
                consolidatedMap[key] = netSum
            } else if (netSum < 0) {
                consolidatedMap[reverseKey] = -netSum
            }
        }
        for ((key, sum) in consolidatedMap) {
            val (participant1, participant2) = key
            consolidatedResults.computeIfAbsent(participant1) { mutableListOf() }.add(DebtPair(sum, participant2))
        }
        return consolidatedResults.map { (participant, debts) ->
            EventResult(participant, debts)
        }.toMutableList()
    }

    private fun sortDebts(listEventResult: List<EventResult>): List<EventResult> {
        return listEventResult.sortedBy { it.participant.name }
    }
}