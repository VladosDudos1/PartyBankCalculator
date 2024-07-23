package vlados.dudos.domain.utils

import android.content.Context
import vlados.dudos.domain.R

object MapHolder {
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
}