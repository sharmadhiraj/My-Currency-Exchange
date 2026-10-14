package com.sharmadhiraj.mycurrencyexchange.ui.converter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sharmadhiraj.mycurrencyexchange.R
import com.sharmadhiraj.mycurrencyexchange.databinding.ItemConvertedAmountBinding
import com.sharmadhiraj.mycurrencyexchange.domain.model.ConvertedAmount
import com.sharmadhiraj.mycurrencyexchange.ui.util.formatAmount

class ConvertedAmountsAdapter :
    ListAdapter<ConvertedAmount, ConvertedAmountsAdapter.ViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemConvertedAmountBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(private val binding: ItemConvertedAmountBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ConvertedAmount) {
            val amount = formatAmount(item.amount)
            binding.textCode.text = item.code
            binding.textName.text = item.name
            binding.textAmount.text = amount
            binding.root.contentDescription =
                binding.root.context.getString(R.string.converted_amount_description, item.name, amount, item.code)
        }
    }

    private object DiffCallback : DiffUtil.ItemCallback<ConvertedAmount>() {
        override fun areItemsTheSame(oldItem: ConvertedAmount, newItem: ConvertedAmount) =
            oldItem.code == newItem.code

        override fun areContentsTheSame(oldItem: ConvertedAmount, newItem: ConvertedAmount) =
            oldItem == newItem
    }
}
