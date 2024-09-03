package vlados.dudos.domain.utils


object ListOperationsSupport {
    fun getMaxId(list: List<Int>): Int {
        return if (list.isNotEmpty()) list.max() + 1 else 0
    }
    fun <T> mergingLists(vararg list: List<T>) : List<T>{
        val resultList = mutableListOf<T>()
        list.forEach {
            it.forEach { element ->
                resultList.add(element)
            }
        }
        return resultList
    }
}