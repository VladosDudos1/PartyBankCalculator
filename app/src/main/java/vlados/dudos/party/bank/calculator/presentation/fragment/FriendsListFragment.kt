package vlados.dudos.party.bank.calculator.presentation.fragment

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import vlados.dudos.domain.model.Participant
import vlados.dudos.domain.utils.ActionHolder.setActionId
import vlados.dudos.domain.utils.ListOperationsSupport.getMaxId
import vlados.dudos.domain.utils.ListOperationsSupport.getMinId
import vlados.dudos.party.bank.calculator.R
import vlados.dudos.party.bank.calculator.app.App
import vlados.dudos.party.bank.calculator.databinding.EditFriendDialogBinding
import vlados.dudos.party.bank.calculator.databinding.FragmentFriendsListBinding
import vlados.dudos.party.bank.calculator.interfaces.INavigateChange
import vlados.dudos.party.bank.calculator.presentation.adapter.FriendsAdapter
import vlados.dudos.party.bank.calculator.presentation.fragment.base.BaseFragment
import vlados.dudos.party.bank.calculator.presentation.viewmodel.HostViewModel

class FriendsListFragment : BaseFragment(), INavigateChange,
    FriendsAdapter.OnClick {

    override fun clickDelete(
        list: MutableList<Participant>,
        recyclerView: RecyclerView,
        position: Int
    ) {
        App.sharedManager.saveFriends(list[position], true)
        hostViewModel.deleteFriendFromEvents(list[position])
        setAdapter()
    }

    override fun clickEdit(
        list: List<Participant>,
        recyclerView: RecyclerView,
        position: Int
    ) {
        showEditFriendDialog(list.toMutableList(), recyclerView, list[position])
        binding.friendsRecycler.adapter?.notifyItemChanged(position)
    }

    private val binding: FragmentFriendsListBinding by lazy {
        FragmentFriendsListBinding.inflate(
            layoutInflater
        )
    }
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
    }

    override fun applyClick() {
        with(binding) {
            goBackLayout.setOnClickListener {
                activity().onBackPressed()
            }
            addButton.setOnClickListener {
                addFriend()
            }
        }
    }

    override fun setAdapter() {
        with(binding) {
            friendsRecycler.layoutManager = LinearLayoutManager(context())
            friendsRecycler.adapter = FriendsAdapter(
                context(),
                App.sharedManager.getFriendsList(),
                this@FriendsListFragment,
                friendsRecycler
            )
        }
    }

    override fun putNavigateId() {
        setActionId(R.id.action_friendsListFragment_to_listEventFragment)
    }

    private fun addFriend() {
        val dialogBinding = EditFriendDialogBinding.inflate(layoutInflater)
        val dialog = Dialog(context(), R.style.CustomDialogTheme).apply {
            setCancelable(true)
            setContentView(dialogBinding.root)
            dialogBinding.okButton.setOnClickListener {
                val participantNameEdited = dialogBinding.nameEditText.text.toString()
                if (participantNameEdited.isNotEmpty()) {
                    App.sharedManager.saveFriends(
                        Participant(
                            getMinId(
                                App.sharedManager.getFriendsList().map { it.id }),
                            participantNameEdited
                        ), false
                    )
                    dismiss()
                } else dialogBinding.inputLayout.helperText =
                    context().getString(R.string.name_cant_be_empty)
            }
        }
        dialog.setOnDismissListener {
            setAdapter()
        }
        dialog.show()
    }

    private fun showEditFriendDialog(
        listParticipant: MutableList<Participant>,
        recyclerView: RecyclerView,
        participant: Participant
    ) {
        val dialogBinding = EditFriendDialogBinding.inflate(layoutInflater)
        val listOfFriends = App.sharedManager.getFriendsList()
        val dialog = Dialog(context(), R.style.CustomDialogTheme).apply {
            setCancelable(true)
            setContentView(dialogBinding.root)
            dialogBinding.nameEditText.setText(participant.name)
            dialogBinding.okButton.setOnClickListener {
                val participantNameEdited = dialogBinding.nameEditText.text.toString()
                if (participantNameEdited.isNotEmpty()) {
                    listParticipant.forEach {
                        if (it.id == participant.id) {
                            it.name = participantNameEdited
                            hostViewModel.editFriendInEvents(participant, participantNameEdited)
                            App.sharedManager.saveFriends(it, false)
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