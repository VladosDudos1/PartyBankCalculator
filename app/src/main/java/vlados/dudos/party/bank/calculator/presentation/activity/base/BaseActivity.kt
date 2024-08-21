package vlados.dudos.party.bank.calculator.presentation.activity.base

import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import vlados.dudos.domain.utils.ActionHolder.getActionId
import vlados.dudos.domain.utils.ActionHolder.setActionId
import vlados.dudos.party.bank.calculator.R

open class BaseActivity : AppCompatActivity() {
    fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    open fun navigate(action: Int) {}
    override fun onBackPressed() {
        val actionId = getActionId()
        if (actionId != -1) {
            setActionId(-1)
            navigate(actionId)
        }
        else super.onBackPressed()
    }
}