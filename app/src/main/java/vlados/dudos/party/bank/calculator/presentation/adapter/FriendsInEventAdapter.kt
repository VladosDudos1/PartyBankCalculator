package vlados.dudos.party.bank.calculator.presentation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import vlados.dudos.domain.model.Participant
import vlados.dudos.party.bank.calculator.databinding.FriendViewCheckboxBinding

class FriendsInEventAdapter(
    val context: Context, val list: MutableList<Participant>, val onClick: OnClick
) : RecyclerView.Adapter<FriendsInEventAdapter.FriendsViewHolder>() {
    private var listSelected = listOf<Int>()

    constructor(
        context: Context,
        list: MutableList<Participant>,
        onClick: OnClick,
        listSelected: List<Int>
    ) : this(context, list, onClick) {
        this.listSelected = listSelected
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendsViewHolder {
        return FriendsViewHolder(
            FriendViewCheckboxBinding.inflate(
                LayoutInflater.from(context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: FriendsViewHolder, position: Int) {
        with(holder.binding) {
            if (listSelected.contains(list[position].id)) checkbox.isChecked = true
            participantNameTxt.text = list[position].name
            root.setOnClickListener {
                checkbox.isChecked = !checkbox.isChecked
            }
            checkbox.setOnCheckedChangeListener { _, isChecked ->
                onClick.click(list, position, isChecked)
            }
        }
    }

    override fun getItemCount(): Int = list.size

    interface OnClick {
        fun click(list: MutableList<Participant>, position: Int, isChecked: Boolean)
    }

    class FriendsViewHolder(val binding: FriendViewCheckboxBinding) :
        RecyclerView.ViewHolder(binding.root)
}