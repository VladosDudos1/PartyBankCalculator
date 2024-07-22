package vlados.dudos.domain.utils

import vlados.dudos.domain.system.SharedManager

object ListOperationsSupport {
    fun getMaxId(list: List<Int>) : Int{
        return if (list.isNotEmpty()) list.max()+1 else 0
    }
}