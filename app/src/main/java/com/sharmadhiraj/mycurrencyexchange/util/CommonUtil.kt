package com.sharmadhiraj.mycurrencyexchange.util

import android.util.Log
import java.text.SimpleDateFormat
import java.util.Locale

object CommonUtil {
    fun formatDateTime(timestamp: Long): String {
        return try {
            val format = SimpleDateFormat("HH:mm:ss, dd MMMM yyyy", Locale.ENGLISH)
            format.format(timestamp * 1000)
        } catch (e: Exception) {
            Log.e("Exception", e.message ?: "")
            ""
        }
    }

    fun stringToDouble(text: String): Double {
        return try {
            text.toDouble()
        } catch (e: Exception) {
            0.0
        }
    }

    fun convertCurrency(
        amount: Double,
        selectedCurrency: String,
        exchangeRates: Map<String, Double>
    ): Map<String, Double> {
        return exchangeRates.mapValues { (_, rate) ->
            amount * rate / exchangeRates[selectedCurrency]!!
        }
    }
}