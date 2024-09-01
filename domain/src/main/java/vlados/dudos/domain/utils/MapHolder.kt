package vlados.dudos.domain.utils

import android.content.Context
import vlados.dudos.domain.R
import vlados.dudos.domain.model.DebtPair
import vlados.dudos.domain.model.Participant

object MapHolder {
    private var mapOfAdditionalSpend = mutableListOf<DebtPair>()
    fun getValueSign(context: Context, valueName: String): String {
        val mapOfValues = mapOf(
            context.getString(R.string.Ruble) to "₽",
            context.getString(R.string.Dollar) to "$",
            context.getString(R.string.Euro) to "€",
            context.getString(R.string.Pound) to "£",
            context.getString(R.string.Yen) to "¥",
        )
        return if (mapOfValues.keys.contains(valueName)) mapOfValues.getValue(valueName) else "₽"
    }
    fun getMapOfValues(context: Context) : Map<String, String>{
        val mapOfValues = mapOf(
            context.getString(R.string.Ruble) to "₽",
            context.getString(R.string.Dollar) to "$",
            context.getString(R.string.Euro) to "€",
            context.getString(R.string.Pound) to "£",
            context.getString(R.string.Yen) to "¥",
        )
        return mapOfValues
    }

    fun addToMapAdditionalSpend(
        participant: Participant,
        price: Double
    ) {
        if (participant !in mapOfAdditionalSpend.map { it.debtor }) mapOfAdditionalSpend.add(DebtPair(price, participant))
        else {
            mapOfAdditionalSpend.forEach {
                if (it.debtor == participant) it.moneySum = price
            }
        }
    }

    fun setFullMapAdditionalSpending(list: List<DebtPair>){
        mapOfAdditionalSpend = list.toMutableList()
    }

    fun removeDebtorFromAdditionalMap(participant: Participant) {
        mapOfAdditionalSpend.removeIf {
            it.debtor == participant
        }
    }

    fun clearMapAdditionalSpend() {
        mapOfAdditionalSpend.clear()
    }

    fun getMapAdditionalSpending(): MutableList<DebtPair> = mapOfAdditionalSpend
}