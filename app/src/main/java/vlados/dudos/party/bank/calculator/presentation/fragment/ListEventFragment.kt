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

class ListEventFragment : BaseFragment() {

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
        with(binding){
            addEventButton.setOnClickListener {
                showAddEventDialog()
            }
        }
    }

    private fun setupRecyclerView() {
        val events = App.sharedManager.getListEvents()
        with(binding) {
            if (events.isEmpty()) {
                eventRecyclerView.visibility = View.GONE
                noEventsTxt.visibility = View.VISIBLE
            } else setAdapter(events)
        }
    }

    private fun setAdapter(events: List<Event>) {
        with(binding) {
            eventRecyclerView.layoutManager = LinearLayoutManager(context())
            eventRecyclerView.adapter = EventAdapter(context(), events)
            noEventsTxt.visibility = View.GONE
        }
    }

    override fun setObservers() {
        viewModel.isFirstLaunch.observe(viewLifecycleOwner){
            if (it) showEnterNameDialog()
            else setupRecyclerView()
        }
    }

    override fun updateUI() {
        viewModel.isFirstLaunch.value = App.sharedManager.isFirstLaunch()
        binding.eventRecyclerView.adapter = EventAdapter(context(), App.sharedManager.getListEvents())
    }
}