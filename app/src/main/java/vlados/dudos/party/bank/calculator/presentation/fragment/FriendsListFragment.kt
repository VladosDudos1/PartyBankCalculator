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
import vlados.dudos.party.bank.calculator.presentation.viewmodel.FriendsViewModel
import vlados.dudos.party.bank.calculator.presentation.viewmodel.HostViewModel

class FriendsListFragment : BaseFragment(), INavigateChange,
    FriendsAdapter.OnClick {

    private val dialogAddFriend: Dialog by lazy { createDialogAddFriend() }
    private val dialogEditFriend: Dialog by lazy { createDialogEditFriend(App.sharedManager.getFriendsList()) }
    private val binding: FragmentFriendsListBinding by lazy {
        FragmentFriendsListBinding.inflate(
            layoutInflater
        )
    }
    private val dialogEditBinding: EditFriendDialogBinding by lazy {
        EditFriendDialogBinding.inflate(
            layoutInflater
        )
    }
    private val hostViewModel: HostViewModel by activityViewModels()
    private val viewModel: FriendsViewModel by viewModels()

    override fun clickDelete(
        list: MutableList<Participant>,
        position: Int
    ) {
        App.sharedManager.saveFriends(list[position], true)
        hostViewModel.deleteFriendFromEvents(list[position])
        setAdapter()
    }

    override fun clickEdit(
        list: List<Participant>,
        position: Int
    ) {
        viewModel.setEditingFriend(list[position])
        showEditFriendDialog()
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
    }

    override fun setObservers() {
        super.setObservers()
        viewModel.editingFriend.observe(viewLifecycleOwner) {
            editEditFriendDialog()
        }
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
                this@FriendsListFragment
            )
        }
    }

    override fun putNavigateId() {
        setActionId(R.id.action_friendsListFragment_to_listEventFragment)
    }

    private fun createDialogAddFriend(): Dialog {
        val dialogBinding = EditFriendDialogBinding.inflate(layoutInflater)
        val dialog = Dialog(context(), R.style.CustomDialogTheme).apply {
            setCancelable(true)
            setContentView(dialogBinding.root)
            setOnDismissListener {
                setAdapter()
                dialogBinding.nameEditText.text?.clear()
            }
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
        return dialog
    }

    private fun addFriend() {
        dialogAddFriend.show()
    }

    private fun createDialogEditFriend(
        listParticipant: MutableList<Participant>
    ): Dialog {
        val dialog = Dialog(context(), R.style.CustomDialogTheme).apply {
            setCancelable(true)
            setContentView(dialogEditBinding.root)
            setOnDismissListener {
                setAdapter()
            }
            dialogEditBinding.okButton.setOnClickListener {
                val participantNameEdited = dialogEditBinding.nameEditText.text.toString()
                if (participantNameEdited.isNotEmpty()) {
                    listParticipant.forEach {
                        if (it.id == viewModel.editingFriend.value?.id) {
                            it.name = participantNameEdited
                            hostViewModel.editFriendInEvents(
                                viewModel.editingFriend.value!!,
                                participantNameEdited
                            )
                            App.sharedManager.saveFriends(it, false)
                        }
                    }
                    dismiss()
                } else dialogEditBinding.inputLayout.helperText =
                    context().getString(R.string.name_cant_be_empty)
            }
        }
        return dialog
    }

    private fun editEditFriendDialog() {
        dialogEditBinding.nameEditText.setText(viewModel.editingFriend.value?.name ?: "")
    }

    private fun showEditFriendDialog() {
        dialogEditFriend.show()
    }
}