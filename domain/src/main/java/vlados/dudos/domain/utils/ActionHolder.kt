package vlados.dudos.domain.utils

object ActionHolder {
    private var actionId: Int = -1

    fun setActionId(id: Int) {
        actionId = id
    }
    fun getActionId() = actionId
}