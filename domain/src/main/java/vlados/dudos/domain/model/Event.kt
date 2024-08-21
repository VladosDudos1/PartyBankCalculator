package vlados.dudos.domain.model

import android.graphics.Bitmap

data class Event(
    var id: Int,
    var name: String,
    var participants: List<Participant>,
    var sum: Int,
    var owner: Participant,
    var listPurchases: MutableList<Purchase>,
    var image: Bitmap? = null
)
