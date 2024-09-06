package vlados.dudos.party.bank.calculator.presentation.fragment

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import vlados.dudos.domain.model.Event
import vlados.dudos.domain.model.Participant
import vlados.dudos.domain.utils.ActionHolder.setActionId
import vlados.dudos.domain.utils.ListOperationsSupport.mergingLists
import vlados.dudos.domain.utils.ModelsTransformUtil.createNewEvent
import vlados.dudos.domain.utils.StringOperationsSupport.isOnlySpace
import vlados.dudos.party.bank.calculator.R
import vlados.dudos.party.bank.calculator.app.App
import vlados.dudos.party.bank.calculator.databinding.EditFriendDialogBinding
import vlados.dudos.party.bank.calculator.databinding.FragmentAddEventBinding
import vlados.dudos.party.bank.calculator.interfaces.INavigateChange
import vlados.dudos.party.bank.calculator.presentation.adapter.FriendsInEventAdapter
import vlados.dudos.party.bank.calculator.presentation.adapter.ParticipantAdapter
import vlados.dudos.party.bank.calculator.presentation.fragment.base.BaseFragment
import vlados.dudos.party.bank.calculator.presentation.viewmodel.AddEventViewModel
import vlados.dudos.party.bank.calculator.presentation.viewmodel.HostViewModel


class AddEventFragment : BaseFragment(),
    ParticipantAdapter.OnClick, FriendsInEventAdapter.OnClick, INavigateChange {

    override fun click(list: MutableList<Participant>, position: Int, isChecked: Boolean) {
        if (isChecked) {
            listFriendsInEvent.add(list[position])
        } else {
            listFriendsInEvent.remove(list[position])
        }
    }

    private val viewModel: AddEventViewModel by viewModels()
    private val hostViewModel: HostViewModel by activityViewModels()
    private val listParticipant =
        mutableListOf(Participant(0, App.sharedManager.getOwnerName()))
    private val listFriendsInEvent =
        mutableListOf<Participant>()
    private val isRedactMode: Boolean by lazy { hostViewModel.isEventExist() }
    private val binding: FragmentAddEventBinding by lazy {
        FragmentAddEventBinding.inflate(
            layoutInflater
        )
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
        setupView()
    }
    override fun applyClick() {
        with(binding) {
            addParticipantToEventButton.setOnClickListener {
                showAddParticipantDialog(listParticipant, participantRecycler)
            }
            confirmButton.setOnClickListener {
                if (isRedactMode) saveExistedEvent() else saveNewEvent()
            }
        }
    }

    override fun setAdapter() {
        super.setAdapter()
        with(binding) {
            participantRecycler.layoutManager = LinearLayoutManager(context())
            participantRecycler.adapter =
                ParticipantAdapter(
                    listParticipant,
                    context(),
                    this@AddEventFragment,
                    participantRecycler
                )
            friendsRecycler.layoutManager = LinearLayoutManager(context())
            friendsRecycler.adapter = FriendsInEventAdapter(
                context(),
                App.sharedManager.getFriendsList(),
                this@AddEventFragment
            )
        }
    }

    override fun setAdapter(event: Event) {
        super.setAdapter(event)
        val friends = App.sharedManager.getFriendsList()
        val friendsIds = friends.map { it.id }
        event.participants.forEach {
            if (it.id !in friendsIds && it.id != event.owner.id) listParticipant.add(it)
            else if (it.id != event.owner.id) listFriendsInEvent.add(it)
        }
        with(binding) {
            participantRecycler.layoutManager = LinearLayoutManager(context())
            participantRecycler.adapter =
                ParticipantAdapter(
                    listParticipant,
                    context(),
                    this@AddEventFragment,
                    participantRecycler
                )
            friendsRecycler.layoutManager = LinearLayoutManager(context())
            friendsRecycler.adapter = FriendsInEventAdapter(
                context(),
                friends,
                this@AddEventFragment,
                event.participants.filter {it.id in friendsIds}
            )
        }
    }
    override fun clickDelete(
        list: MutableList<Participant>,
        recyclerView: RecyclerView,
        participant: Participant
    ) {
        list.remove(participant)
        hostViewModel.deleteParticipantFromEvent(participant)
        recyclerView.adapter?.notifyDataSetChanged()
    }

    override fun clickEdit(
        list: List<Participant>,
        recyclerView: RecyclerView,
        participant: Participant
    ) {
        showEditParticipantDialog(list.toMutableList(), recyclerView, participant)
    }

    private fun setupView() {
        with(binding) {
            if (isRedactMode) {
                nameEditText.setText(hostViewModel.selectedItem.value!!.name)
                setAdapter(hostViewModel.selectedItem.value!!)
            } else setAdapter()
        }
    }

    private fun saveNewEvent() {
        if (!binding.nameEditText.text.toString().isOnlySpace()) {
            App.sharedManager.saveNewEvent(
                createNewEvent(
                    App.sharedManager.getListEvents().map { it.id },
                    binding.nameEditText.text.toString(),
                    mergingLists(
                        listFriendsInEvent,
                        listParticipant
                    ).sortedByDescending { it.id == listParticipant[0].id },
                    listParticipant[0]
                )
            )
            activity().onBackPressed()
        } else binding.inputLayout.helperText =
            context().getString(R.string.name_cant_be_empty)
    }

    private fun saveExistedEvent() {
        if (!binding.nameEditText.text.toString().isOnlySpace()) {
            App.sharedManager.changeCurrentEvent(changeExistingEvent())
            activity().onBackPressed()
        } else binding.inputLayout.helperText =
            context().getString(R.string.name_cant_be_empty)
    }

    private fun changeExistingEvent(): Event {
        hostViewModel.selectedItem.value!!.name = binding.nameEditText.text.toString()
        hostViewModel.selectedItem.value!!.participants = mergingLists(
            listFriendsInEvent,
            listParticipant
        ).sortedByDescending { it.name == listParticipant[0].name }
        return hostViewModel.selectedItem.value!!
    }

    override fun putNavigateId() {
        setActionId(if (isRedactMode) R.id.action_addEventFragment_to_eventFragment else R.id.action_addEventFragment_to_listEventFragment)
    }
    private fun showEditParticipantDialog(
        listParticipant: MutableList<Participant>,
        recyclerView: RecyclerView,
        participant: Participant
    ) {
        val dialogBinding = EditFriendDialogBinding.inflate(layoutInflater)
        val dialog = Dialog(context(), R.style.CustomDialogTheme).apply {
            setCancelable(true)
            setContentView(dialogBinding.root)
            dialogBinding.nameEditText.setText(participant.name)
            dialogBinding.okButton.setOnClickListener {
                val participantNameEdited = dialogBinding.nameEditText.text.toString()
                if (!participantNameEdited.isOnlySpace()) {
                    listParticipant.forEach {
                        if (it.id == participant.id) {
                            hostViewModel.editParticipantInEvent(it, participantNameEdited)
                            it.name = participantNameEdited
                        }
                    }
                    dismiss()
                } else dialogBinding.inputLayout.helperText =
                    context().getString(R.string.name_cant_be_empty)
            }
        }
        dialog.setOnDismissListener {
            recyclerView.adapter?.notifyDataSetChanged()
        }
        dialog.show()
    }
}