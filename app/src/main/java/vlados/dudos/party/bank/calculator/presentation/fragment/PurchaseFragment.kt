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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import vlados.dudos.domain.model.Event
import vlados.dudos.domain.model.Participant
import vlados.dudos.domain.utils.ActionHolder.setActionId
import vlados.dudos.domain.utils.ListOperationsSupport.getTransList
import vlados.dudos.domain.utils.StringOperationsSupport.correctTextAsCounter
import vlados.dudos.party.bank.calculator.R
import vlados.dudos.party.bank.calculator.app.App
import vlados.dudos.party.bank.calculator.databinding.FragmentPurchaseBinding
import vlados.dudos.party.bank.calculator.interfaces.IActiveFragment
import vlados.dudos.party.bank.calculator.interfaces.INavigateChange
import vlados.dudos.party.bank.calculator.presentation.adapter.BuyerSelectAdapter
import vlados.dudos.party.bank.calculator.presentation.adapter.UserSelectAdapter
import vlados.dudos.party.bank.calculator.presentation.fragment.base.BaseFragment
import vlados.dudos.party.bank.calculator.presentation.viewmodel.HostViewModel
import vlados.dudos.party.bank.calculator.presentation.viewmodel.PurchaseViewModel


class PurchaseFragment : BaseFragment(), IActiveFragment, TextWatcher,
    BuyerSelectAdapter.OnClick, INavigateChange {

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
        putNavigateId()
        hostViewModel.generatePurchase()
        applyTextWatcher(binding.costEditText, this)
        setObservers()
        setupView()
        setAdapters()
        applyClick()
    }

    override fun putNavigateId() {
        setActionId(R.id.action_purchaseFragment_to_eventFragment)
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
            confirmButton.setOnClickListener {
                if (checkAllInfoFilled()) {
                    val cost = costEditText.text.toString().toInt().toDouble()
                    val name = purchaseNameEditText.text.toString()
                    hostViewModel.savePurchase(cost, name)
                    navigate(R.id.action_purchaseFragment_to_eventFragment)
                }
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
                makeAdditionalSpend()
            }
        }
    }

    private fun setAdapters() {
        with(binding) {
            val participants = getCurrentEvent().participants
            buyerRecycler.layoutManager = GridLayoutManager(context(), 2)
            buyerRecycler.adapter = BuyerSelectAdapter(
                context(),
                participants,
                this@PurchaseFragment,
                participants.indexOf(hostViewModel.getCurrentPurchase().buyer)
            )

            debtorsRecycler.layoutManager = GridLayoutManager(context(), 2)
            debtorsRecycler.adapter =
                UserSelectAdapter(context(), participants)
        }
    }

    private fun checkAllInfoFilled(): Boolean {
        with(binding) {
            val cost = costEditText.text.toString().toInt()
            val name = purchaseNameEditText.text.toString()
            when {
                name.isEmpty() -> showToast(getString(R.string.enter_name_of_purchase))
                cost == 0 -> showToast(getString(R.string.enter_cost_of_the_purchase))
                !hostViewModel.isNewPurchaseFilled() -> showToast(getString(R.string.purchase_must_have_buyer_and_debtors))
                else -> {
                    return true
                }
            }
            return false
        }
    }

    private fun setupView() {
        binding.purchaseNameEditText.setText(hostViewModel.getName())
        binding.costEditText.setText(hostViewModel.getCost().toString())
    }

    private fun getCurrentEvent(): Event {
        return hostViewModel.selectedItem.value!!
    }

    private fun makeAdditionalSpend() {
        with(binding) {
            val cost = costEditText.text.toString().toInt()
            val name = purchaseNameEditText.text.toString()
            hostViewModel.setCost(cost)
            hostViewModel.setName(name)
            navigate(R.id.action_purchaseFragment_to_additionalPriceFragment)
        }
    }

    override fun afterTextChanged(s: Editable?) {}
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
}