package vlados.dudos.party.bank.calculator.presentation.fragment

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.ContextThemeWrapper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.launch
import vlados.dudos.domain.model.Event
import vlados.dudos.domain.model.Participant
import vlados.dudos.domain.model.Purchase
import vlados.dudos.domain.utils.ActionHolder.setActionId
import vlados.dudos.domain.utils.ModelsTransformUtil.createNewEvent
import vlados.dudos.domain.utils.ModelsTransformUtil.listParticipantsToString
import vlados.dudos.party.bank.calculator.R
import vlados.dudos.party.bank.calculator.app.App
import vlados.dudos.party.bank.calculator.databinding.AddEventLayoutBinding
import vlados.dudos.party.bank.calculator.databinding.ChangeEventNameDialogBinding
import vlados.dudos.party.bank.calculator.databinding.FragmentEventBinding
import vlados.dudos.party.bank.calculator.databinding.ListParticipantDialogBinding
import vlados.dudos.party.bank.calculator.interfaces.IActiveFragment
import vlados.dudos.party.bank.calculator.interfaces.INavigateChange
import vlados.dudos.party.bank.calculator.presentation.adapter.ParticipantAdapter
import vlados.dudos.party.bank.calculator.presentation.adapter.PurchaseAdapter
import vlados.dudos.party.bank.calculator.presentation.fragment.base.BaseFragment
import vlados.dudos.party.bank.calculator.presentation.viewmodel.EventViewModel
import vlados.dudos.party.bank.calculator.presentation.viewmodel.HostViewModel


class EventFragment : BaseFragment(), IActiveFragment, INavigateChange, PurchaseAdapter.OnClick {

    private val binding: FragmentEventBinding by lazy { FragmentEventBinding.inflate(layoutInflater) }
    private val viewModel: EventViewModel by viewModels()
    private val hostViewModel: HostViewModel by activityViewModels()

    override fun deletePurchase(purchase: Purchase) {
        viewModel.deletePurchase(purchase, context(), layoutInflater, getCurrentEvent())
    }

    override fun redactPurchase(purchase: Purchase) {
        hostViewModel.setNewPurchase(purchase)
        navigate(R.id.action_eventFragment_to_purchaseFragment)
    }

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
        setupEvent(getCurrentEvent())
    }

    override fun putNavigateId() {
        setActionId(R.id.action_eventFragment_to_listEventFragment)
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
        setAdapter(event)
    }

    private fun getCurrentEvent(): Event {
        return hostViewModel.selectedItem.value!!
    }

    override fun applyClick() {
        with(binding) {
            addPurchaseBtn.setOnClickListener {
                addPurchase()
            }
            addParticipantBtn.setOnClickListener {
                openParticipantDialog()
            }
            optionsEventButton.setOnClickListener {
                openPopupMenuOptions()
            }
        }
    }

    private fun addPurchase() {
        hostViewModel.generatePurchase()
        navigate(R.id.action_eventFragment_to_purchaseFragment)
    }

    override fun setObservers() {
        viewModel.purchaseDeleted.observe(viewLifecycleOwner) {
            updateAdapter()
        }
    }

    override fun updateUi() {
        setupEvent(getCurrentEvent())
    }

    override fun setAdapter(event: Event) {
        with(binding) {
            listPurchasesRecycler.layoutManager = LinearLayoutManager(context())
            listPurchasesRecycler.adapter =
                PurchaseAdapter(context(), event.listPurchases, this@EventFragment)
        }
    }

    private fun updateAdapter() {
        with(binding) {
            listPurchasesRecycler.adapter?.notifyDataSetChanged()
        }
    }

    private fun openParticipantDialog() {
        val listParticipant = getCurrentEvent().participants.toMutableList()
        val dialogBinding = ListParticipantDialogBinding.inflate(layoutInflater)
        val dialog = Dialog(context(), R.style.CustomDialogTheme).apply {
            setCancelable(true)
            setContentView(dialogBinding.root)
            with(dialogBinding) {
                participantRecycler.layoutManager = LinearLayoutManager(context())
                participantRecycler.adapter =
                    ParticipantAdapter(
                        listParticipant,
                        context(),
                        this@EventFragment,
                        participantRecycler
                    )
                addParticipantBtn.setOnClickListener {
                    showAddParticipantDialog(listParticipant, participantRecycler)
                }
            }
        }
        dialog.setOnDismissListener {
            val event = getCurrentEvent()
            event.participants = listParticipant
            App.sharedManager.changeCurrentEvent(event)
            updateUi()
        }
        dialog.show()
    }

    private fun openChangeNameDialog() {
        val event = getCurrentEvent()
        val dialogBinding = ChangeEventNameDialogBinding.inflate(layoutInflater)
        val dialog = Dialog(context(), R.style.CustomDialogTheme).apply {
            setCancelable(true)
            setContentView(dialogBinding.root)
            with(dialogBinding) {
                positiveButton.setOnClickListener {
                    if (nameEditText.text.toString().isNotEmpty()) {
                        event.name = dialogBinding.nameEditText.text.toString()
                        App.sharedManager.changeCurrentEvent(event)
                        updateUi()
                        dismiss()
                    } else dialogBinding.inputLayout.helperText =
                        context().getString(R.string.name_cant_be_empty)
                }
            }
        }
        dialog.show()
    }

    private fun deleteCurrentEvent() {
        App.sharedManager.deleteEvent(getCurrentEvent())
        navigate(R.id.action_eventFragment_to_listEventFragment)
    }

    private fun openPopupMenuOptions() {
        val wrapper: Context = ContextThemeWrapper(requireContext(), R.style.popupMenuStyle)
        val popup = PopupMenu(wrapper, binding.optionsEventButton)

        popup.inflate(R.menu.options_menu)
        popup.setOnMenuItemClickListener {
            optionProcessing(it.toString())
            true
        }
        popup.show()
    }

    private fun optionProcessing(option: String) {
        when (option) {
            getString(R.string.change_event_name) -> openChangeNameDialog()
            getString(R.string.delete_event) -> deleteCurrentEvent()
        }
    }
}