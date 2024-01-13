package com.sharmadhiraj.mycurrencyexchange.ui.converter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sharmadhiraj.mycurrencyexchange.R
import com.sharmadhiraj.mycurrencyexchange.domain.model.Currency
import java.text.DecimalFormat

class CurrenciesAdapter : RecyclerView.Adapter<CurrenciesAdapter.ViewHolder>() {

    private var currencies: List<Currency> = emptyList()

    fun setExchangeRates(updatedCurrencies: List<Currency>) {
        currencies = updatedCurrencies
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_exchange_rate, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textCurrencyCode.text = currencies[position].name
        holder.textExchangeRate.text =
            currencies[position].code + " " + DecimalFormat("#,##0.00").format(currencies[position].rate)
    }

    override fun getItemCount(): Int = currencies.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textCurrencyCode: TextView = itemView.findViewById(R.id.textCurrencyCode)
        val textExchangeRate: TextView = itemView.findViewById(R.id.textExchangeRate)
    }
}
