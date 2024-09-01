package vlados.dudos.party.bank.calculator.presentation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import vlados.dudos.party.bank.calculator.databinding.ValueViewBinding

class ValueAdapter(val context: Context, val listValues: Map<String, String>, val onClick: OnClick) : RecyclerView.Adapter<ValueAdapter.ValueViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ValueViewHolder {
        return ValueViewHolder(
            ValueViewBinding.inflate(
                LayoutInflater.from(context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ValueViewHolder, position: Int) {
        with(holder.binding){
            valueName.text = listValues.keys.toList()[position]
            valueSign.text = listValues.values.toList()[position]
            root.setOnClickListener {
                onClick.clickValue(listValues.values.toList()[position])
            }
        }
    }

    override fun getItemCount(): Int = listValues.keys.size

    class ValueViewHolder(val binding: ValueViewBinding) : RecyclerView.ViewHolder(binding.root)

    interface OnClick{
        fun clickValue(value: String)
    }
}