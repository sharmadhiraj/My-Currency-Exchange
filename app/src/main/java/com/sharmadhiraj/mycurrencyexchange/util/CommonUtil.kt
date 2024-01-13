package com.sharmadhiraj.mycurrencyexchange.util

import android.util.Log
import com.sharmadhiraj.mycurrencyexchange.domain.model.Currency
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

    fun convertCurrencies(
        amount: Double,
        selectedCurrency: String,
        exchangeRates: List<Currency>
    ): List<Currency> {
        val baseRateFromCurrency: Double = getBaseRateForCurrency(exchangeRates, selectedCurrency)
        return exchangeRates.map { e ->
            Currency(
                e.code,
                e.name,
                convertCurrency(amount, baseRateFromCurrency, e.rate)
            )
        }
    }

    private fun convertCurrency(
        amount: Double,
        baseRateFromCurrency: Double,
        baseRateToCurrency: Double
    ): Double {
        return amount * baseRateToCurrency / baseRateFromCurrency
    }

    private fun getBaseRateForCurrency(
        exchangeRates: List<Currency>,
        selectedCurrency: String
    ): Double {
        return exchangeRates.firstOrNull { e -> e.fullName() == selectedCurrency }?.rate ?: 1.0
    }

}