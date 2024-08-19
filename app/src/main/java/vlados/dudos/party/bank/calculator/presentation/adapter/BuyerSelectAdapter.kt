package vlados.dudos.party.bank.calculator.presentation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import vlados.dudos.domain.model.Participant
import vlados.dudos.party.bank.calculator.R
import vlados.dudos.party.bank.calculator.databinding.SelectUserLayoutBinding


class BuyerSelectAdapter(
    val context: Context,
    val list: List<Participant>,
    private val onClick: BuyerSelectAdapter.OnClick,
    var selectedPosition: Int = 0
) :
    RecyclerView.Adapter<BuyerSelectAdapter.UserSelectViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserSelectViewHolder {
        return UserSelectViewHolder(
            SelectUserLayoutBinding.inflate(
                LayoutInflater.from(context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: UserSelectViewHolder, position: Int) {
        with(holder.binding) {
            nameParticipant.text = list[position].name
            holder.setItem(list[position], selectedPosition, context)
            if (position == 0){
                holder.setItem(list[position], selectedPosition, context)
                onClick.selectParticipant(buyer = list[position])
            }
            root.setOnClickListener {
                onClick.selectParticipant(buyer = list[position])
                notifyItemChanged(selectedPosition)
                notifyItemChanged(position)
                selectedPosition=position
            }
        }
    }

    override fun getItemCount(): Int = list.size

    interface OnClick {
        fun selectParticipant(
            buyer: Participant
        )
    }



    class UserSelectViewHolder(val binding: SelectUserLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun setItem(item: Participant, selectedPosition: Int, context: Context) {
            setColors(selectedPosition != adapterPosition, binding.root, binding.nameParticipant, context)
        }
        private fun setColors(isActive: Boolean, cardView: CardView, textView: TextView, context: Context) {
            cardView.setCardBackgroundColor(context.getColor(if (!isActive) R.color.alternative else R.color.softMainColor))
            textView.setBackgroundColor(context.getColor(if (!isActive) R.color.alternative else R.color.softMainColor))
            textView.setTextColor(context.getColor(if (isActive) R.color.textColor else R.color.textColorAlternative))
        }
    }
}