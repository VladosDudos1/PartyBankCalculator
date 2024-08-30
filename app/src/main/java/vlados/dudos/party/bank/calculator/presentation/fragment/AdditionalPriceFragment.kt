package vlados.dudos.party.bank.calculator.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import vlados.dudos.domain.model.DebtPair
import vlados.dudos.domain.utils.ActionHolder.setActionId
import vlados.dudos.domain.utils.ListOperationsSupport.getTransList
import vlados.dudos.domain.utils.MapHolder.getMapAdditionalSpending
import vlados.dudos.domain.utils.MapHolder.setFullMapAdditionalSpending
import vlados.dudos.party.bank.calculator.R
import vlados.dudos.party.bank.calculator.app.App
import vlados.dudos.party.bank.calculator.databinding.FragmentAdditionalPriceBinding
import vlados.dudos.party.bank.calculator.databinding.FragmentEventBinding
import vlados.dudos.party.bank.calculator.interfaces.IActiveFragment
import vlados.dudos.party.bank.calculator.interfaces.INavigateChange
import vlados.dudos.party.bank.calculator.presentation.adapter.AdditionalSpendsAdapter
import vlados.dudos.party.bank.calculator.presentation.fragment.base.BaseFragment
import vlados.dudos.party.bank.calculator.presentation.viewmodel.HostViewModel

class AdditionalPriceFragment : BaseFragment(), IActiveFragment, INavigateChange {

    private val binding: FragmentAdditionalPriceBinding by lazy { FragmentAdditionalPriceBinding.inflate(layoutInflater) }
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
    private fun setAdapter(){
        with(binding){
            additionalSpendRecycler.layoutManager = LinearLayoutManager(context())
            if (hostViewModel.getCurrentPurchase().additionalDebts.isNotEmpty()) setFullMapAdditionalSpending(hostViewModel.getCurrentPurchase().additionalDebts)
            additionalSpendRecycler.adapter =
                AdditionalSpendsAdapter(context(), getMapAdditionalSpending())
        }
    }

    override fun updateUi() {}
    override fun setObservers() {}
    override fun putNavigateId() {
        setActionId(R.id.action_additionalPriceFragment_to_purchaseFragment)
    }
}