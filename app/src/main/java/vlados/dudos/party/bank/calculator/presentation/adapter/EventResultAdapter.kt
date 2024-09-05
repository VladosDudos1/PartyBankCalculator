package vlados.dudos.party.bank.calculator.presentation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import vlados.dudos.domain.model.EventResult
import vlados.dudos.domain.utils.ModelsTransformUtil.eventResultToString
import vlados.dudos.party.bank.calculator.R
import vlados.dudos.party.bank.calculator.app.App
import vlados.dudos.party.bank.calculator.databinding.EventResultViewBinding

class EventResultAdapter(val context: Context, val list: List<EventResult>, val shareEvent: ShareEvent) :
    RecyclerView.Adapter<EventResultAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            EventResultViewBinding.inflate(
                LayoutInflater.from(context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder.binding) {
            val stringEventResult = eventResultToString(
                "${context.getString(R.string.debtors)} ",
                list[position],
                App.sharedManager.getBaseValue()
            )
            buyerText.text = list[position].participant.name
            debtsText.text = stringEventResult

            shareBtn.setOnClickListener {
                shareEvent.share("${list[position].participant.name.uppercase()}\n${stringEventResult}")
            }
        }
    }

    interface ShareEvent{
        fun share(resString: String)
    }
    override fun getItemCount(): Int = list.size

    class ViewHolder(val binding: EventResultViewBinding) : RecyclerView.ViewHolder(binding.root)
}