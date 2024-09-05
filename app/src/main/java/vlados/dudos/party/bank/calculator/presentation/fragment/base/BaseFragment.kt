package vlados.dudos.party.bank.calculator.presentation.fragment.base

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import vlados.dudos.domain.model.Event
import vlados.dudos.domain.model.Participant
import vlados.dudos.domain.utils.ListOperationsSupport.getMaxId
import vlados.dudos.domain.utils.ModelsTransformUtil.listEventResultToString
import vlados.dudos.party.bank.calculator.R
import vlados.dudos.party.bank.calculator.app.App
import vlados.dudos.party.bank.calculator.databinding.EditFriendDialogBinding
import vlados.dudos.party.bank.calculator.databinding.NameInputLayoutBinding
import vlados.dudos.party.bank.calculator.databinding.ResultDialogBinding
import vlados.dudos.party.bank.calculator.presentation.activity.base.BaseActivity
import vlados.dudos.party.bank.calculator.presentation.adapter.EventResultAdapter

abstract class BaseFragment : Fragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        applyClick()
        setObservers()
        setAdapter()
    }

    protected fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    protected fun context(): Context = requireContext()

    protected fun activity(): BaseActivity = requireActivity() as BaseActivity

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
        val dialogBinding = EditFriendDialogBinding.inflate(layoutInflater)
        val dialog = Dialog(context(), R.style.CustomDialogTheme).apply {
            setCancelable(true)
            setContentView(dialogBinding.root)
            dialogBinding.okButton.setOnClickListener {
                val participantName = dialogBinding.nameEditText.text.toString()
                if (participantName.isNotEmpty()) {
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
        val dialogBinding = EditFriendDialogBinding.inflate(layoutInflater)
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

    open fun showEventResultDialog(event: Event) {
        val listEventResult = App.calculateManager.calculateDebts(event)
        val dialogBinding = ResultDialogBinding.inflate(layoutInflater)
        val dialog = Dialog(context(), R.style.CustomDialogTheme).apply {
            setCancelable(true)
            setContentView(dialogBinding.root)
            dialogBinding.debtorsRecycler.layoutManager = LinearLayoutManager(context())
            dialogBinding.debtorsRecycler.adapter = EventResultAdapter(
                context(),
                listEventResult,
                object : EventResultAdapter.ShareEvent {
                    override fun share(resString: String) {
                        shareText(resString)
                    }
                })

            dialogBinding.shareBtn.setOnClickListener {
                shareText(
                    listEventResultToString(
                        context().getString(R.string.debtors),
                        listEventResult,
                        App.sharedManager.getBaseValue()
                    )
                )
            }
        }
        dialog.show()
    }

    protected fun navigate(action: Int) {
        findNavController().navigate(action)
    }

    open fun setAdapter() {}
    open fun setAdapter(event: Event) {}
    open fun setAdapter(events: List<Event>) {}

    protected fun applyTextWatcher(editText: EditText, textWatcher: TextWatcher) {
        editText.addTextChangedListener(textWatcher)
    }

    open fun setObservers() {

    }

    open fun applyClick() {

    }

    open fun updateUi() {}

    private fun shareText(string: String) {
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, string)
            type = "text/plain"
        }
        startActivity(
            Intent.createChooser(
                shareIntent,
                getString(R.string.share_calculation)
            )
        )
    }
}