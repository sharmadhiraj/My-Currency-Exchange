package com.sharmadhiraj.mycurrencyexchange.ui.converter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sharmadhiraj.mycurrencyexchange.databinding.ItemExchangeRateBinding
import com.sharmadhiraj.mycurrencyexchange.domain.model.Currency

class CurrenciesAdapter :
    ListAdapter<Currency, CurrenciesAdapter.ViewHolder>(CurrencyDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemExchangeRateBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currency = getItem(position)
        holder.bind(currency)
    }

    fun setExchangeRates(exchangeRates: List<Currency>) {
        submitList(exchangeRates)
    }

    inner class ViewHolder(private val binding: ItemExchangeRateBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(currency: Currency) {
            binding.currency = currency
            binding.executePendingBindings()
        }
    }

    private class CurrencyDiffCallback : DiffUtil.ItemCallback<Currency>() {
        override fun areItemsTheSame(oldItem: Currency, newItem: Currency): Boolean {
            return oldItem.code == newItem.code
        }

        override fun areContentsTheSame(oldItem: Currency, newItem: Currency): Boolean {
            return oldItem == newItem
        }
    }
}
