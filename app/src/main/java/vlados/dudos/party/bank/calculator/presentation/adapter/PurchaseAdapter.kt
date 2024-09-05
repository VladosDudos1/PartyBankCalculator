package vlados.dudos.party.bank.calculator.presentation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import vlados.dudos.domain.model.Purchase
import vlados.dudos.domain.utils.ModelsTransformUtil.listParticipantsToString
import vlados.dudos.domain.utils.ModelsTransformUtil.listParticipantsToStringWithAdditionalDebts
import vlados.dudos.party.bank.calculator.databinding.EventListItemBinding
import vlados.dudos.party.bank.calculator.databinding.PurchaseListItemBinding
import kotlin.math.cos

class PurchaseAdapter(private val context: Context, private val list: List<Purchase>, val onClick: OnClick) :
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
            purchaseNameText.text = list[position].name
            buyerNameText.text = list[position].buyer.name
            listParticipantsText.text = listParticipantsToStringWithAdditionalDebts(list[position].listDebtors, list[position].additionalDebts)
            costText.text = list[position].cost.toInt().toString()
            root.setOnClickListener {
                onClick.redactPurchase(list[position])
            }
            root.setOnLongClickListener {
                onClick.deletePurchase(list[position])
                false
            }
        }
    }

    interface OnClick{
        fun deletePurchase(purchase: Purchase)
        fun redactPurchase(purchase: Purchase)
    }

    override fun getItemCount(): Int = list.size

    class PurchaseViewHolder(val binding: PurchaseListItemBinding) :
        RecyclerView.ViewHolder(binding.root)
}