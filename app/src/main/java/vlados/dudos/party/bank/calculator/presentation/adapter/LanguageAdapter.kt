package vlados.dudos.party.bank.calculator.presentation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import vlados.dudos.party.bank.calculator.databinding.LanguageViewBinding
import vlados.dudos.party.bank.calculator.databinding.ValueViewBinding
import vlados.dudos.party.bank.calculator.presentation.adapter.LanguageAdapter.LanguageViewHolder

class LanguageAdapter(val context: Context, val listValues: Map<Int, String>, val onClick: OnClick) : RecyclerView.Adapter<LanguageAdapter.LanguageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LanguageViewHolder {
        return LanguageViewHolder(
            LanguageViewBinding.inflate(
                LayoutInflater.from(context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: LanguageViewHolder, position: Int) {
        with(holder.binding){
            languageName.text = context.getString(listValues.keys.toList()[position])
            languageName.setOnClickListener {
                onClick.clickLanguage(listValues.values.toList()[position].substring(0..1))
            }
        }
    }

    override fun getItemCount(): Int = listValues.size

    class LanguageViewHolder(val binding: LanguageViewBinding) : RecyclerView.ViewHolder(binding.root)

    interface OnClick{
        fun clickLanguage(language: String)
    }
}