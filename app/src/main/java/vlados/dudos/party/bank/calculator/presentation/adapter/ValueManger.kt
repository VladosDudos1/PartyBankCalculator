package vlados.dudos.party.bank.calculator.presentation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import vlados.dudos.party.bank.calculator.databinding.ValueViewBinding

class ValueManger(val context: Context, val listValues: Map<String, String>, val onClick: OnClick) : RecyclerView.Adapter<ValueManger.ValueViewHolder>() {

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

    }

    override fun getItemCount(): Int = listValues.keys.size

    class ValueViewHolder(val binding: ValueViewBinding) : RecyclerView.ViewHolder(binding.root)

    interface OnClick{
        fun click(value: String)
    }
}