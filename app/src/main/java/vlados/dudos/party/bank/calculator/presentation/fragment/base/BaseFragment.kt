package vlados.dudos.party.bank.calculator.presentation.fragment.base

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import vlados.dudos.party.bank.calculator.R
import vlados.dudos.party.bank.calculator.app.App
import vlados.dudos.party.bank.calculator.databinding.NameInputLayoutBinding

abstract class BaseFragment : Fragment() {
    protected fun showToast(message: String) {
        Toast.makeText(requireContext(), "", Toast.LENGTH_SHORT).show()
    }

    protected fun context(): Context {
        return requireContext()
    }

    protected fun activity(): Activity {
        return requireActivity()
    }

    protected fun showEnterNameDialog() {
        val dialogBinding = NameInputLayoutBinding.inflate(layoutInflater)
        val dialog = Dialog(context(), R.style.CustomDialogTheme).apply {
            setCancelable(false)
            setContentView(dialogBinding.root)
            dialogBinding.positiveButton.setOnClickListener {
                val ownerName = dialogBinding.nameEditText.text.toString()
                if (ownerName.isNotEmpty()) {
                    App.sharedManager.endFirstLaunch(ownerName)
                    dismiss()
                }
                else dialogBinding.inputLayout.helperText = context().getString(R.string.name_cant_be_empty)
            }
        }
        dialog.show()
    }

    abstract fun applyClick()
    abstract fun setObservers()
}