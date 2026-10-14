package com.sharmadhiraj.mycurrencyexchange.ui.converter

import android.content.Context
import android.widget.ArrayAdapter
import android.widget.Filter
import com.sharmadhiraj.mycurrencyexchange.domain.model.Currency

/** Dropdown adapter whose filter matches anywhere in "CODE - Name", so users can search by either. */
class CurrencyDropdownAdapter(context: Context, currencies: List<Currency>) :
    ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, currencies.map(::label)) {

    private val allLabels = currencies.map(::label)

    override fun getFilter(): Filter = object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val query = constraint?.toString()?.trim().orEmpty()
            val matches = if (query.isEmpty()) allLabels
            else allLabels.filter { it.contains(query, ignoreCase = true) }
            return FilterResults().apply {
                values = matches
                count = matches.size
            }
        }

        @Suppress("UNCHECKED_CAST")
        override fun publishResults(constraint: CharSequence?, results: FilterResults) {
            clear()
            addAll(results.values as List<String>)
            notifyDataSetChanged()
        }

        override fun convertResultToString(resultValue: Any?) = resultValue.toString()
    }

    companion object {
        fun label(currency: Currency) = "${currency.code} - ${currency.name}"

        fun codeOf(label: String) = label.substringBefore(" - ")
    }
}
