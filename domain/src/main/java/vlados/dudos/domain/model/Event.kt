package vlados.dudos.domain.model

data class Event(
    var id: Int,
    var name: String,
    var participants: List<Participant>,
    var sum: Int,
    var owner: Participant,
    var listPurchases: List<Purchase>
)
