package vlados.dudos.party.bank.calculator.presentation.fragment.base

import android.app.Activity
import android.content.Context
import android.widget.Toast
import androidx.fragment.app.Fragment

abstract class BaseFragment : Fragment() {
    protected fun showToast(message: String){
        Toast.makeText(requireContext(), "", Toast.LENGTH_SHORT).show()
    }
    protected fun context() : Context {
        return requireContext()
    }
    protected fun activity() : Activity {
        return requireActivity()
    }
    abstract fun applyClick()
}