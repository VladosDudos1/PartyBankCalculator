package vlados.dudos.party.bank.calculator.presentation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import vlados.dudos.domain.model.EventResult
import vlados.dudos.domain.utils.ModelsTransformUtil.listEventResultToString
import vlados.dudos.party.bank.calculator.R
import vlados.dudos.party.bank.calculator.app.App
import vlados.dudos.party.bank.calculator.databinding.EventResultViewBinding

class EventResultAdapter(val context: Context, val list: List<EventResult>) :
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
            buyerText.text = list[position].participant.name

            debtsText.text =
                listEventResultToString(
                    "${context.getString(R.string.debtors)} ",
                    list[position],
                    App.sharedManager.getBaseValue()
                )
        }
    }

    override fun getItemCount(): Int = list.size

    class ViewHolder(val binding: EventResultViewBinding) : RecyclerView.ViewHolder(binding.root)
}