package vlados.dudos.domain.model

data class Purchase(
    var id: Int,
    var name: String,
    var cost: Double,
    var buyer: Participant,
    var listDebtors: MutableList<Participant>,
    var additionalDebts: MutableList<DebtPair> = mutableListOf()
)
