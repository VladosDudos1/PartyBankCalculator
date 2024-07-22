package vlados.dudos.party.bank.calculator.presentation.fragment.base

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import vlados.dudos.domain.model.Event
import vlados.dudos.domain.model.Participant
import vlados.dudos.domain.utils.ListOperationsSupport.getMaxId
import vlados.dudos.domain.utils.ModelsTransformUtil.createNewEvent
import vlados.dudos.party.bank.calculator.R
import vlados.dudos.party.bank.calculator.app.App
import vlados.dudos.party.bank.calculator.databinding.AddEventLayoutBinding
import vlados.dudos.party.bank.calculator.databinding.FriendAddLayoutBinding
import vlados.dudos.party.bank.calculator.databinding.NameInputLayoutBinding
import vlados.dudos.party.bank.calculator.interfaces.IUpdateUi
import vlados.dudos.party.bank.calculator.presentation.adapter.ParticipantAdapter
import kotlin.math.truncate

abstract class BaseFragment : Fragment(), ParticipantAdapter.OnClick, IUpdateUi {
    protected fun showToast(message: String) {
        Toast.makeText(requireContext(), "", Toast.LENGTH_SHORT).show()
    }


    protected fun context(): Context = requireContext()

    protected fun activity(): Activity = requireActivity()

    protected fun showEnterNameDialog() {
        val dialogBinding = NameInputLayoutBinding.inflate(layoutInflater)
        val dialog = Dialog(context(), R.style.CustomDialogTheme).apply {
            setCancelable(false)
            setContentView(dialogBinding.root)
            dialogBinding.positiveButton.setOnClickListener {
                val ownerName = dialogBinding.nameEditText.text.toString()
                if (ownerName.isNotEmpty()) {
                    App.sharedManager.endFirstLaunch(ownerName)
                    updateUi()
                    dismiss()
                } else dialogBinding.inputLayout.helperText =
                    context().getString(R.string.name_cant_be_empty)
            }
        }
        dialog.show()
    }

    protected fun showAddParticipantDialog(
        listParticipant: MutableList<Participant>,
        recyclerView: RecyclerView
    ) {
        val dialogBinding = FriendAddLayoutBinding.inflate(layoutInflater)
        val dialog = Dialog(context(), R.style.CustomDialogTheme).apply {
            setCancelable(true)
            setContentView(dialogBinding.root)
            dialogBinding.okButton.setOnClickListener {
                val participantName = dialogBinding.nameEditText.text.toString()
                if (participantName.isNotEmpty()) {
                    if (dialogBinding.addFriendCheckBox.isChecked) App.sharedManager.saveFriends(
                        friend = Participant(
                            getMaxId(
                                App.sharedManager.getFriendsList().map { it.id }), participantName
                        ),
                        isDelete = false
                    )
                    listParticipant.add(
                        Participant(
                            getMaxId(listParticipant.map { it.id }),
                            participantName
                        )
                    )
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

    protected fun showEditParticipantDialog(
        listParticipant: MutableList<Participant>,
        recyclerView: RecyclerView,
        participant: Participant
    ) {
        val dialogBinding = FriendAddLayoutBinding.inflate(layoutInflater)
        val listOfFriends = App.sharedManager.getFriendsList()
        val isInFriends: Boolean = participant.name in listOfFriends.map { it.name }
        val dialog = Dialog(context(), R.style.CustomDialogTheme).apply {
            setCancelable(true)
            setContentView(dialogBinding.root)
            dialogBinding.addFriendCheckBox.isChecked = isInFriends
            dialogBinding.nameEditText.setText(participant.name)
            dialogBinding.okButton.setOnClickListener {
                val participantNameEdited = dialogBinding.nameEditText.text.toString()
                if (participantNameEdited.isNotEmpty()) {
                    if (dialogBinding.addFriendCheckBox.isChecked && !isInFriends) {
                        App.sharedManager.saveFriends(
                            friend = Participant(
                                getMaxId(
                                    listOfFriends.map { it.id }),
                                participantNameEdited
                            ),
                            isDelete = false
                        )
                    } else if (!dialogBinding.addFriendCheckBox.isChecked && isInFriends) {
                        deleteFriend(participant.name)
                    }
                    listParticipant.forEach {
                        if (it.id == participant.id) {
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

    protected fun showAddEventDialog() {
        val listParticipant =
            mutableListOf<Participant>(Participant(0, App.sharedManager.getOwnerName()))
        val dialogBinding = AddEventLayoutBinding.inflate(layoutInflater)
        val dialog = Dialog(context(), R.style.CustomDialogTheme).apply {
            setCancelable(true)
            setContentView(dialogBinding.root)
            with(dialogBinding) {
                participantRecycler.layoutManager = LinearLayoutManager(context())
                participantRecycler.adapter =
                    ParticipantAdapter(
                        listParticipant,
                        context(),
                        this@BaseFragment,
                        participantRecycler
                    )
                addParticipantLayout.setOnClickListener {
                    showAddParticipantDialog(listParticipant, participantRecycler)
                }
                positiveButton.setOnClickListener {
                    if (nameEditText.text.toString().isNotEmpty()) {
                        App.sharedManager.saveNewEvent(
                            createNewEvent(
                                App.sharedManager.getListEvents().map { it.id },
                                nameEditText.text.toString(),
                                listParticipant,
                                listParticipant[0]
                            )
                        )
                        dismiss()
                    } else dialogBinding.inputLayout.helperText =
                        context().getString(R.string.name_cant_be_empty)
                }
            }
        }
        dialog.setOnDismissListener {
            updateUi()
        }
        dialog.show()
    }

    override fun clickDelete(
        list: MutableList<Participant>,
        recyclerView: RecyclerView,
        participant: Participant
    ) {
        list.remove(participant)
        recyclerView.adapter?.notifyDataSetChanged()
    }

    override fun clickEdit(
        list: List<Participant>,
        recyclerView: RecyclerView,
        participant: Participant
    ) {
        showEditParticipantDialog(list.toMutableList(), recyclerView, participant)
    }

    private fun deleteFriend(friendName: String) {
        App.sharedManager.saveFriends(
            friend = App.sharedManager.getFriendsList().first { it.name == friendName },
            isDelete = true
        )
    }

    protected fun navigate(action: Int) {
        findNavController().navigate(action)
    }
}