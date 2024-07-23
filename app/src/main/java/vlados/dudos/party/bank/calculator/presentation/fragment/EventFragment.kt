package vlados.dudos.party.bank.calculator.presentation.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import vlados.dudos.domain.model.Event
import vlados.dudos.domain.utils.ModelsTransformUtil.listParticipantsToString
import vlados.dudos.party.bank.calculator.R
import vlados.dudos.party.bank.calculator.app.App
import vlados.dudos.party.bank.calculator.databinding.FragmentEventBinding
import vlados.dudos.party.bank.calculator.interfaces.IActiveFragment
import vlados.dudos.party.bank.calculator.presentation.adapter.PurchaseAdapter
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
        setObservers()
    }


    private fun setupEvent(event: Event) {
        with(binding) {
            eventNameTxt.text = event.name
            ownerTxt.text = getString(R.string.organizer, event.owner.name)
            sumTxt.text =
                getString(R.string.sum, event.sum.toString(), App.sharedManager.getBaseValue())
            participantsTxt.text = getString(
                R.string.participants,
                listParticipantsToString(event.participants)
            )
        }
    }

    private fun getCurrentEvent(): Event {
        return hostViewModel.selectedItem.value!!
    }

    override fun applyClick() {
        with(binding) {
            addPurchaseBtn.setOnClickListener {

            }
            addParticipantBtn.setOnClickListener {

            }
            imageEvent.setOnClickListener {

            }
        }
    }

    override fun setObservers() {

    }

    override fun updateUi() {

    }

    private fun setAdapter(event: Event) {
        with(binding) {
            listPurchasesRecycler.layoutManager = LinearLayoutManager(context())
            listPurchasesRecycler.adapter = PurchaseAdapter(context(), getCurrentEvent().listPurchases)
        }
    }

    private fun updateAdapter() {
        with(binding) {
            listPurchasesRecycler.adapter?.notifyDataSetChanged()
        }
    }
}