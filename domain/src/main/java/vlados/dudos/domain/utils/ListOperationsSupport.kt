package vlados.dudos.domain.utils


object ListOperationsSupport {
    fun getMaxId(list: List<Int>): Int {
        return if (list.isNotEmpty()) list.max() + 1 else 0
    }

    fun getMinId(list: List<Int>): Int {
        return if (list.isNotEmpty()) list.min() -1 else -1
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
    fun <T> mergingMutableLists(vararg list: MutableList<T>) : MutableList<T>{
        val resultList = mutableListOf<T>()
        list.forEach {
            it.forEach { element ->
                resultList.add(element)
            }
        }
        return resultList
    }
}