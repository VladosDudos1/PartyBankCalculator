package vlados.dudos.party.bank.calculator.presentation.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import vlados.dudos.party.bank.calculator.R
import vlados.dudos.party.bank.calculator.databinding.FragmentPurchaseBinding
import vlados.dudos.party.bank.calculator.interfaces.IActiveFragment
import vlados.dudos.party.bank.calculator.presentation.fragment.base.BaseFragment
import vlados.dudos.party.bank.calculator.presentation.viewmodel.EventViewModel
import vlados.dudos.party.bank.calculator.presentation.viewmodel.HostViewModel
import vlados.dudos.party.bank.calculator.presentation.viewmodel.PurchaseViewModel

class PurchaseFragment : BaseFragment(), IActiveFragment {

    private val binding: FragmentPurchaseBinding by lazy { FragmentPurchaseBinding.inflate(layoutInflater) }
    private val viewModel: PurchaseViewModel by viewModels()
    private val hostViewModel: HostViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun updateUi() { }
    override fun setObservers() {}
    override fun applyClick() {}
}