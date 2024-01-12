package com.sharmadhiraj.mycurrencyexchange.ui.converter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sharmadhiraj.mycurrencyexchange.R
import java.text.DecimalFormat

class ExchangeRateAdapter : RecyclerView.Adapter<ExchangeRateAdapter.ViewHolder>() {

    private var exchangeRates: List<Map.Entry<String, Double>> = emptyList()

    fun setExchangeRates(data: Map<String, Double>) {
        exchangeRates = data.entries.toList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_exchange_rate, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val (currencyCode, rate) = exchangeRates[position]

        holder.textCurrencyCode.text = currencyCode
        holder.textExchangeRate.text = DecimalFormat("#,##0.00").format(rate)
    }

    override fun getItemCount(): Int = exchangeRates.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textCurrencyCode: TextView = itemView.findViewById(R.id.textCurrencyCode)
        val textExchangeRate: TextView = itemView.findViewById(R.id.textExchangeRate)
    }
}
