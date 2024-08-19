package vlados.dudos.party.bank.calculator.presentation.fragment

import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import vlados.dudos.domain.utils.StringOperationsSupport.correctTextAsCounter
import vlados.dudos.party.bank.calculator.R
import vlados.dudos.party.bank.calculator.databinding.FragmentPurchaseBinding
import vlados.dudos.party.bank.calculator.interfaces.IActiveFragment
import vlados.dudos.party.bank.calculator.presentation.fragment.base.BaseFragment
import vlados.dudos.party.bank.calculator.presentation.viewmodel.HostViewModel
import vlados.dudos.party.bank.calculator.presentation.viewmodel.PurchaseViewModel


class PurchaseFragment : BaseFragment(), IActiveFragment, TextWatcher {


    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        with(binding) {
            val maxProgress = costBar.max
            val correctedText = correctTextAsCounter(costEditText.text.toString())
            if (costEditText.text.toString() != correctedText) costEditText.setText(correctedText)
            if (correctedText.toInt() <= maxProgress){
                viewModel.changeSeekProgress(correctedText.toInt())
            }
            else viewModel.changeSeekProgress(maxProgress)
            costEditText.setSelection(correctedText.length)
        }
    }

    private val binding: FragmentPurchaseBinding by lazy {
        FragmentPurchaseBinding.inflate(
            layoutInflater
        )
    }
    private val viewModel: PurchaseViewModel by viewModels()
    private val hostViewModel: HostViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        applyTextWatcher(binding.costEditText, this)
        setObservers()
        applyClick()
    }

    override fun updateUi() {}
    override fun setObservers() {
        with(binding) {
            viewModel.progress.observe(viewLifecycleOwner) {
                if (costBar.progress != costEditText.text.toString().toInt())
                    costBar.progress = it
            }
        }
    }

    override fun applyClick() {
        with(binding) {
            root.setOnClickListener {
                costEditText.clearFocus()
            }
            costBar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
                override fun onStopTrackingTouch(seekBar: SeekBar) {
                }

                override fun onStartTrackingTouch(seekBar: SeekBar) {}

                override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                    if (seekBar.progress > costEditText.text.toString().toInt() || fromUser) costEditText.setText(seekBar.progress.toString())
                }
            })
        }
    }

    override fun afterTextChanged(s: Editable?) {}
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
}