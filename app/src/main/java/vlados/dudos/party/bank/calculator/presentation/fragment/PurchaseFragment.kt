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
import androidx.recyclerview.widget.GridLayoutManager
import vlados.dudos.domain.model.Event
import vlados.dudos.domain.model.Participant
import vlados.dudos.domain.utils.StringOperationsSupport.correctTextAsCounter
import vlados.dudos.party.bank.calculator.R
import vlados.dudos.party.bank.calculator.databinding.FragmentPurchaseBinding
import vlados.dudos.party.bank.calculator.interfaces.IActiveFragment
import vlados.dudos.party.bank.calculator.presentation.adapter.BuyerSelectAdapter
import vlados.dudos.party.bank.calculator.presentation.adapter.UserSelectAdapter
import vlados.dudos.party.bank.calculator.presentation.fragment.base.BaseFragment
import vlados.dudos.party.bank.calculator.presentation.viewmodel.HostViewModel
import vlados.dudos.party.bank.calculator.presentation.viewmodel.PurchaseViewModel


class PurchaseFragment : BaseFragment(), IActiveFragment, TextWatcher, UserSelectAdapter.OnClick, BuyerSelectAdapter.OnClick {

    override fun selectParticipant(listDebtors: List<Participant>) {
        hostViewModel.addDebtorToPurchase(listDebtors)
    }

    override fun selectParticipant(buyer: Participant) {
        hostViewModel.addBuyerToPurchase(buyer)
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        with(binding) {
            val maxProgress = costBar.max
            val correctedText = correctTextAsCounter(costEditText.text.toString())
            if (costEditText.text.toString() != correctedText) costEditText.setText(correctedText)
            if (correctedText.toInt() <= maxProgress) {
                viewModel.changeSeekProgress(correctedText.toInt())
            } else viewModel.changeSeekProgress(maxProgress)
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
        setAdapters()
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
                    if (seekBar.progress > costEditText.text.toString()
                            .toInt() || fromUser
                    ) costEditText.setText(seekBar.progress.toString())
                }
            })
            nextButton.setOnClickListener {
                checkAllInfoFilled()
            }
        }
    }

    private fun setAdapters() {
        with(binding){
            buyerRecycler.layoutManager = GridLayoutManager(context(), 2)
            buyerRecycler.adapter = BuyerSelectAdapter(context(), getCurrentEvent().participants, this@PurchaseFragment, 0)

            debtorsRecycler.layoutManager = GridLayoutManager(context(), 2)
            debtorsRecycler.adapter = UserSelectAdapter(context(), getCurrentEvent().participants, this@PurchaseFragment)
        }
    }

    private fun checkAllInfoFilled() {
        with(binding){
            when{
                purchaseNameEditText.text.toString().isEmpty() -> showToast(getString(R.string.enter_name_of_purchase))
                costEditText.text.toString().toInt() == 0 -> showToast(getString(R.string.enter_cost_of_the_purchase))
                !hostViewModel.isNewPurchaseFilled() -> showToast(getString(R.string.purchase_must_have_buyer_and_debtors))
                else -> showToast("Success")
            }
        }
    }

    private fun getCurrentEvent(): Event {
        return hostViewModel.selectedItem.value!!
    }

    override fun afterTextChanged(s: Editable?) {}
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
}