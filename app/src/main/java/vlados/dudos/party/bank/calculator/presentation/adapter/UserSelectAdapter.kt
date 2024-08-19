package vlados.dudos.party.bank.calculator.presentation.adapter

import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import vlados.dudos.domain.model.Participant
import vlados.dudos.party.bank.calculator.R
import vlados.dudos.party.bank.calculator.databinding.PurchaseListItemBinding
import vlados.dudos.party.bank.calculator.databinding.SelectUserLayoutBinding

class UserSelectAdapter(
    val context: Context,
    val list: List<Participant>,
    private val onClick: OnClick
) :
    RecyclerView.Adapter<UserSelectAdapter.UserSelectViewHolder>() {

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
        val listParticipants = mutableListOf<Participant>()
        with(holder.binding) {
            nameParticipant.text = list[position].name
            root.setOnClickListener {
                if (list[position] in listParticipants) {
                    listParticipants.remove(list[position])
                    holder.setColors(true, root, nameParticipant, context)
                } else {
                    listParticipants.add(list[position])
                    holder.setColors(false, root, nameParticipant, context)
                }
                onClick.selectParticipant(listDebtors = listParticipants)
            }
        }
    }

    override fun getItemCount(): Int = list.size

    interface OnClick {
        fun selectParticipant(listDebtors: List<Participant>)
    }


    class UserSelectViewHolder(val binding: SelectUserLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun setColors(isActive: Boolean, cardView: CardView, textView: TextView, context: Context) {
            cardView.setCardBackgroundColor(context.getColor(if (!isActive) R.color.alternative else R.color.softMainColor))
            textView.setBackgroundColor(context.getColor(if (!isActive) R.color.alternative else R.color.softMainColor))
            textView.setTextColor(context.getColor(if (isActive) R.color.textColor else R.color.textColorAlternative))
        }
    }
}