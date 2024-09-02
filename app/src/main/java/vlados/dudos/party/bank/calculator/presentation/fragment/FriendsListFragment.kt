package vlados.dudos.party.bank.calculator.presentation.fragment

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import vlados.dudos.domain.model.Participant
import vlados.dudos.domain.utils.ActionHolder.setActionId
import vlados.dudos.domain.utils.ListOperationsSupport.getMaxId
import vlados.dudos.party.bank.calculator.R
import vlados.dudos.party.bank.calculator.app.App
import vlados.dudos.party.bank.calculator.databinding.EditFriendDialogBinding
import vlados.dudos.party.bank.calculator.databinding.FragmentFriendsListBinding
import vlados.dudos.party.bank.calculator.databinding.FriendAddLayoutBinding
import vlados.dudos.party.bank.calculator.interfaces.IActiveFragment
import vlados.dudos.party.bank.calculator.interfaces.INavigateChange
import vlados.dudos.party.bank.calculator.presentation.adapter.FriendsAdapter
import vlados.dudos.party.bank.calculator.presentation.fragment.base.BaseFragment

class FriendsListFragment : BaseFragment(), IActiveFragment, INavigateChange,
    FriendsAdapter.OnClick {

    override fun clickDelete(
        list: MutableList<Participant>,
        recyclerView: RecyclerView,
        participant: Participant
    ) {
        list.remove(participant)
        App.sharedManager.saveFriends(participant, true)
        updateUi()
    }

    override fun clickEdit(
        list: List<Participant>,
        recyclerView: RecyclerView,
        participant: Participant
    ) {
        showEditFriendDialog(list.toMutableList(), recyclerView, participant)
    }

    private val binding: FragmentFriendsListBinding by lazy {
        FragmentFriendsListBinding.inflate(
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
        applyClick()
        setAdapter()
        setObservers()
    }

    override fun setObservers() {

    }

    override fun applyClick() {
        with(binding) {
            goBackLayout.setOnClickListener {
                activity().onBackPressed()
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

    override fun updateUi() {
        with(binding){
            if (App.sharedManager.getFriendsList().size > 0) friendsRecycler.adapter?.notifyDataSetChanged()
        }
    }

    override fun putNavigateId() {
        setActionId(R.id.action_friendsListFragment_to_listEventFragment)
    }

    fun showEditFriendDialog(
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
                        }
                        if (it in listOfFriends){
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