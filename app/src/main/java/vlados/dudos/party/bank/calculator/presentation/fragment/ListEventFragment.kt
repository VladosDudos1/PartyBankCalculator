package vlados.dudos.party.bank.calculator.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import vlados.dudos.party.bank.calculator.databinding.FragmentListEventBinding
import vlados.dudos.party.bank.calculator.presentation.fragment.base.BaseFragment
import vlados.dudos.party.bank.calculator.presentation.viewmodel.ListEventViewModel

class ListEventFragment : BaseFragment() {

    private val binding: FragmentListEventBinding by lazy { FragmentListEventBinding.inflate(layoutInflater) }
    private val viewModel: ListEventViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        applyClick()
    }

    override fun applyClick() {

    }
}