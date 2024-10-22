package vlados.dudos.party.bank.calculator.presentation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import vlados.dudos.domain.model.Participant
import vlados.dudos.party.bank.calculator.databinding.FriendViewBinding

class FriendsAdapter(
    val context: Context, val list: List<Participant>, val onClick: OnClick
) : RecyclerView.Adapter<FriendsAdapter.FriendsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendsViewHolder {
        return FriendsViewHolder(
            FriendViewBinding.inflate(
                LayoutInflater.from(context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: FriendsViewHolder, position: Int) {
        with(holder.binding) {
            participantNameTxt.text = list[position].name

            editBtn.setOnClickListener {
                onClick.clickEdit(list, position)
            }
            deleteBtn.setOnClickListener {
                onClick.clickDelete(list.toMutableList(), position)
            }
        }
    }

    override fun getItemCount(): Int = list.size

    interface OnClick {
        fun clickDelete(list: MutableList<Participant>, position: Int)
        fun clickEdit(list: List<Participant>, position: Int)
    }

    class FriendsViewHolder(val binding: FriendViewBinding) : RecyclerView.ViewHolder(binding.root)
}