package vlados.dudos.party.bank.calculator.presentation.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import vlados.dudos.domain.model.Event
import vlados.dudos.party.bank.calculator.R
import vlados.dudos.party.bank.calculator.app.App
import vlados.dudos.party.bank.calculator.databinding.FragmentEventBinding
import vlados.dudos.party.bank.calculator.interfaces.IActiveFragment
import vlados.dudos.party.bank.calculator.presentation.fragment.base.BaseFragment
import vlados.dudos.party.bank.calculator.presentation.viewmodel.EventViewModel
import vlados.dudos.party.bank.calculator.presentation.viewmodel.HostViewModel


class EventFragment : BaseFragment(), IActiveFragment {

    private val binding: FragmentEventBinding by lazy { FragmentEventBinding.inflate(layoutInflater) }
    private val viewModel: EventViewModel by viewModels()
    private val hostViewModel: HostViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupEvent(getCurrentEvent())
        applyClick()
    }

    override fun applyClick() {

    }

    override fun setObservers() {

    }
    override fun updateUi() {

    }
    private fun setupEvent(event: Event) {
        with(binding){
            eventNameTxt.text = event.name
            ownerTxt.text = getString(R.string.organizer, event.owner.name)
            sumTxt.text = getString(R.string.sum, event.sum.toString(), App.sharedManager.getBaseValue())
        }
    }
    private fun getCurrentEvent() : Event{
        return hostViewModel.selectedItem.value!!
    }
}