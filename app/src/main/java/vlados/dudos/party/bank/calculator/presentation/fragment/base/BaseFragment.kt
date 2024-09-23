package vlados.dudos.party.bank.calculator.presentation.fragment.base

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import vlados.dudos.domain.model.Event
import vlados.dudos.domain.model.EventResult
import vlados.dudos.domain.model.Participant
import vlados.dudos.domain.utils.ListOperationsSupport.getMaxId
import vlados.dudos.domain.utils.ModelsTransformUtil.listEventResultToString
import vlados.dudos.domain.utils.StringOperationsSupport.isOnlySpace
import vlados.dudos.domain.utils.StringOperationsSupport.removeSpaces
import vlados.dudos.party.bank.calculator.R
import vlados.dudos.party.bank.calculator.app.App
import vlados.dudos.party.bank.calculator.databinding.EditFriendDialogBinding
import vlados.dudos.party.bank.calculator.databinding.NameInputLayoutBinding
import vlados.dudos.party.bank.calculator.databinding.ResultDialogBinding
import vlados.dudos.party.bank.calculator.presentation.activity.base.BaseActivity
import vlados.dudos.party.bank.calculator.presentation.adapter.EventResultAdapter

abstract class BaseFragment : Fragment() {

    private lateinit var listEventResult: List<EventResult>
    private val dialogEventResult : Dialog by lazy { createEventResultDialog() }

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

    private fun createEventResultDialog() : Dialog{
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
        return dialog
    }

    open fun showEventResultDialog(event: Event) {
        listEventResult = App.calculateManager.calculateDebts(event)
        if (listEventResult.isEmpty()){
            dialogEventResult.dismiss()
            showToast(context().getString(R.string.nothing_to_calculate))
        } else  dialogEventResult.show()
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

    protected fun showFAQ(title: String, message: String){
        val dialog = MaterialAlertDialogBuilder(activity())
            .setTitle(title)
            .setMessage(message)
            .setBackground(AppCompatResources.getDrawable(context(), R.drawable.card_form_no_transperency))
            .show()
    }

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