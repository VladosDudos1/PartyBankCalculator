package vlados.dudos.domain.model

data class EventResult (
    var participant: Participant,
    var listDebts: MutableList<DebtPair>
)