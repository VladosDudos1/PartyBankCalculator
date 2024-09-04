package vlados.dudos.party.bank.calculator.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import vlados.dudos.domain.model.DebtPair
import vlados.dudos.domain.model.Participant
import vlados.dudos.domain.utils.ActionHolder.setActionId
import vlados.dudos.party.bank.calculator.R
import vlados.dudos.party.bank.calculator.app.App
import vlados.dudos.party.bank.calculator.databinding.FragmentAdditionalPriceBinding
import vlados.dudos.party.bank.calculator.databinding.FragmentEventBinding
import vlados.dudos.party.bank.calculator.interfaces.IActiveFragment
import vlados.dudos.party.bank.calculator.interfaces.INavigateChange
import vlados.dudos.party.bank.calculator.presentation.adapter.AdditionalSpendsAdapter
import vlados.dudos.party.bank.calculator.presentation.fragment.base.BaseFragment
import vlados.dudos.party.bank.calculator.presentation.viewmodel.AddEventViewModel
import vlados.dudos.party.bank.calculator.presentation.viewmodel.AdditionalSpendViewModel
import vlados.dudos.party.bank.calculator.presentation.viewmodel.HostViewModel
import kotlin.math.cos

class AdditionalPriceFragment : BaseFragment(), IActiveFragment, INavigateChange, AdditionalSpendsAdapter.OnClick {

    override fun click(participant: Participant, cost: Double) {
        hostViewModel.addToAdditionalSpend(participant, cost)
    }

    private val binding: FragmentAdditionalPriceBinding by lazy { FragmentAdditionalPriceBinding.inflate(layoutInflater) }
    private val viewModel: AdditionalSpendViewModel by viewModels()
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
        applyClick()
        setObservers()
        setAdapter()
    }

    override fun applyClick() {
        with(binding){
            confirmButton.setOnClickListener {
                navigate(R.id.action_additionalPriceFragment_to_purchaseFragment)
            }
        }
    }
    override fun setAdapter(){
        with(binding){
            additionalSpendRecycler.layoutManager = LinearLayoutManager(context())
            if (hostViewModel.getCurrentPurchase().additionalDebts.isNotEmpty()) hostViewModel.setListAdditionalSpending(hostViewModel.getCurrentPurchase().additionalDebts)
            additionalSpendRecycler.adapter =
                AdditionalSpendsAdapter(context(), hostViewModel.getCurrentPurchase().additionalDebts, this@AdditionalPriceFragment)
        }
    }
    override fun setObservers() {

    }
    override fun putNavigateId() {
        setActionId(R.id.action_additionalPriceFragment_to_purchaseFragment)
    }
}