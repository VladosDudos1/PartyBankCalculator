package vlados.dudos.party.bank.calculator.presentation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import vlados.dudos.domain.model.DebtPair
import vlados.dudos.domain.model.Participant
import vlados.dudos.party.bank.calculator.R
import vlados.dudos.party.bank.calculator.databinding.SelectUserLayoutBinding

class UserSelectAdapter(
    private val context: Context,
    private val list: List<Participant>,
    private val listDebtors: MutableList<Participant>,
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
        with(holder.binding) {
            nameParticipant.text = list[position].name
            if (list[position] in listDebtors){
                holder.setColors(false, root, nameParticipant, context)
            }
            root.setOnClickListener {
                if (list[position] in listDebtors) {
                    onClick.click(true, list[position])
                    holder.setColors(true, root, nameParticipant, context)
                } else {
                    onClick.click(false, list[position])
                    holder.setColors(false, root, nameParticipant, context)
                }
            }
        }
    }

    override fun getItemCount(): Int = list.size
    interface OnClick{
        fun click(isActive: Boolean, participant: Participant)
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