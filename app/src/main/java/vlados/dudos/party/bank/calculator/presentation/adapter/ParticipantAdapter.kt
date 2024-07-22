package vlados.dudos.party.bank.calculator.presentation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import vlados.dudos.domain.model.Participant
import vlados.dudos.party.bank.calculator.databinding.ParticipantElementBinding

class ParticipantAdapter(
    private val list: MutableList<Participant>,
    private val context: Context,
    private val onClick: OnClick,
    private val rv: RecyclerView
) :
    RecyclerView.Adapter<ParticipantAdapter.ParticipantViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParticipantViewHolder {
        return ParticipantViewHolder(
            ParticipantElementBinding.inflate(
                LayoutInflater.from(context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ParticipantViewHolder, position: Int) {
        with(holder.binding) {
            participantNameTxt.text = list[position + 1].name
            editBtn.setOnClickListener {
                onClick.clickEdit(list, rv, list[position + 1])
            }
            deleteBtn.setOnClickListener {
                onClick.clickDelete(list, rv, list[position+1])
            }
        }
    }

    override fun getItemCount(): Int = list.size - 1

    class ParticipantViewHolder(val binding: ParticipantElementBinding) :
        RecyclerView.ViewHolder(binding.root)

    interface OnClick {
        fun clickDelete(list: MutableList<Participant>, recyclerView: RecyclerView, participant: Participant)
        fun clickEdit(list: List<Participant>, recyclerView: RecyclerView, participant: Participant)
    }
}