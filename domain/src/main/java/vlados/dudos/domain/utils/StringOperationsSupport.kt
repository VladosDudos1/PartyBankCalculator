package vlados.dudos.domain.utils

import android.util.Log

object StringOperationsSupport {
    fun correctTextAsCounter(text: String): String {
        var resultString = text
        if (text.isEmpty()) {
            resultString = "0"
        } else if (text != "0" && text[0].toString() == "0"
        ) {
            correctTextAsCounter(text.removeRange(0..0))
        }
        return resultString.toInt().toString()
    }
}