package com.sharmadhiraj.mycurrencyexchange.ui.util

import java.text.DateFormat
import java.text.NumberFormat
import java.util.Date
import java.util.Locale

private class AmountFormats(val locale: Locale) {
    val standard: NumberFormat = create(maxFractionDigits = 2)
    val small: NumberFormat = create(maxFractionDigits = 6)

    private fun create(maxFractionDigits: Int) = NumberFormat.getNumberInstance(locale).apply {
        minimumFractionDigits = 2
        maximumFractionDigits = maxFractionDigits
    }
}

private var amountFormats = AmountFormats(Locale.getDefault())

fun parseAmount(input: String): Double? {
    return input.trim().replace(',', '.').toDoubleOrNull()?.takeIf { it.isFinite() && it >= 0 }
}

fun formatAmount(amount: Double): String {
    val locale = Locale.getDefault()
    if (amountFormats.locale != locale) amountFormats = AmountFormats(locale)
    val formats = amountFormats
    return (if (amount != 0.0 && amount < 1) formats.small else formats.standard).format(amount)
}

fun formatTimestamp(millis: Long): String {
    return DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT).format(Date(millis))
}
