package vlados.dudos.party.bank.calculator.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import vlados.dudos.domain.model.Event
import vlados.dudos.party.bank.calculator.app.App
import vlados.dudos.party.bank.calculator.databinding.FragmentListEventBinding
import vlados.dudos.party.bank.calculator.presentation.adapter.EventAdapter
import vlados.dudos.party.bank.calculator.presentation.fragment.base.BaseFragment
import vlados.dudos.party.bank.calculator.presentation.viewmodel.ListEventViewModel

class ListEventFragment : BaseFragment(), EventAdapter.OnClick {
    override fun clickOnEvent() {

    }

    private val binding: FragmentListEventBinding by lazy {
        FragmentListEventBinding.inflate(
            layoutInflater
        )
    }
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
        setObservers()
    }

    override fun applyClick() {
        with(binding) {
            addEventButton.setOnClickListener {
                showAddEventDialog()
            }
        }
    }
    private fun setAdapter(events: List<Event>) {
        with(binding) {
            eventRecyclerView.layoutManager = LinearLayoutManager(context())
            eventRecyclerView.adapter = EventAdapter(context(), events, this@ListEventFragment)
        }
    }

    override fun setObservers() {
        with(binding){
            viewModel.isFirstLaunch.observe(viewLifecycleOwner) {
                if (it) showEnterNameDialog()
            }
            viewModel.eventsList.observe(viewLifecycleOwner){
                if (it.size == 0)
                    noEventsTxt.visibility = View.VISIBLE
                else {
                    noEventsTxt.visibility = View.GONE
                }
                setAdapter(it)
            }
        }
    }

    override fun updateUI() {
        viewModel.isFirstLaunch.value = App.sharedManager.isFirstLaunch()
        viewModel.updateEventList()
    }
}