package vlados.dudos.party.bank.calculator.presentation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import vlados.dudos.domain.model.Purchase
import vlados.dudos.party.bank.calculator.databinding.EventListItemBinding
import vlados.dudos.party.bank.calculator.databinding.PurchaseListItemBinding

class PurchaseAdapter(private val context: Context, private val list: List<Purchase>) :
    RecyclerView.Adapter<PurchaseAdapter.PurchaseViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PurchaseViewHolder {
        return PurchaseViewHolder(
            PurchaseListItemBinding.inflate(
                LayoutInflater.from(context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: PurchaseViewHolder, position: Int) {
        with(holder.binding){

        }
    }

    override fun getItemCount(): Int = list.size

    class PurchaseViewHolder(val binding: PurchaseListItemBinding) :
        RecyclerView.ViewHolder(binding.root)
}