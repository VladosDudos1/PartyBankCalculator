package vlados.dudos.party.bank.calculator.presentation.adapter

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.recyclerview.widget.RecyclerView
import vlados.dudos.domain.model.DebtPair
import vlados.dudos.domain.model.Participant
import vlados.dudos.domain.utils.StringOperationsSupport.correctTextAsCounter
import vlados.dudos.party.bank.calculator.databinding.AdditionalSpendItemBinding

class AdditionalSpendsAdapter(val context: Context, val list: List<DebtPair>, private val onClick: OnClick) : RecyclerView.Adapter<AdditionalSpendsAdapter.AdditionalViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdditionalViewHolder {
        return AdditionalViewHolder(
            AdditionalSpendItemBinding.inflate(
                LayoutInflater.from(context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: AdditionalViewHolder, position: Int) {
        with(holder.binding) {
            nameParticipantText.text = list[position].debtor.name
            costBar.progress = list[position].moneySum.toInt()
            costEditText.setText(list[position].moneySum.toInt().toString())
            costBar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener{
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    if (seekBar!!.progress > costEditText.text.toString()
                            .toInt() || fromUser
                    ) costEditText.setText(seekBar.progress.toString())
                }
                override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                    onClick.click(list[holder.adapterPosition].debtor, costEditText.text.toString().toDouble())
                }
            })
        }
    }
    override fun getItemCount(): Int = list.size

    class AdditionalViewHolder(val binding: AdditionalSpendItemBinding) : RecyclerView.ViewHolder(binding.root), TextWatcher{
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }
        override fun afterTextChanged(s: Editable?) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            with(binding) {
                val maxProgress = costBar.max
                val correctedText = correctTextAsCounter(costEditText.text.toString())
                if (costEditText.text.toString() != correctedText) costEditText.setText(correctedText)
                if (correctedText.toInt() <= maxProgress) {
                    costBar.progress = correctedText.toInt()
                } else costBar.progress = maxProgress
                costEditText.setSelection(correctedText.length)
            }
        }
    }
    interface OnClick{
        fun click(participant: Participant, cost: Double)
    }
}