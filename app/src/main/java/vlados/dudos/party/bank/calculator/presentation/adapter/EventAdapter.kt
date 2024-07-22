package vlados.dudos.party.bank.calculator.presentation.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import vlados.dudos.domain.model.Event
import vlados.dudos.domain.utils.ModelsTransformUtil
import vlados.dudos.domain.utils.ModelsTransformUtil.listParticipantsToString
import vlados.dudos.party.bank.calculator.R
import vlados.dudos.party.bank.calculator.databinding.EventListItemBinding

class EventAdapter(private val context: Context, private val listEvents: List<Event>, private val onClick: OnClick) : RecyclerView.Adapter<EventAdapter.EventViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        return EventViewHolder(
            EventListItemBinding.inflate(
                LayoutInflater.from(context),
                parent,
                false
            )
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        with(holder.binding){
            sumTxt.text = context.getString(R.string.sum, listEvents[position].sum.toString(), context.getString(R.string.rubble))
            eventNameTxt.text = listEvents[position].name
            ownerTxt.text = context.getString(R.string.organizer, listEvents[position].owner.name)
            participantsTxt.text = context.getString(
                R.string.participants,
                listParticipantsToString(listEvents[position].participants)
            )
            parentLayout.setOnClickListener {
                onClick.clickOnEvent()
            }
        }
    }

    override fun getItemCount(): Int = listEvents.size

    interface OnClick{
        fun clickOnEvent()
    }

    class EventViewHolder(val binding: EventListItemBinding) : RecyclerView.ViewHolder(binding.root)
}