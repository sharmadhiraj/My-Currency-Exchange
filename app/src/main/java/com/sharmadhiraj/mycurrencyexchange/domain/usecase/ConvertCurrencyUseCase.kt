package com.sharmadhiraj.mycurrencyexchange.domain.usecase

import com.sharmadhiraj.mycurrencyexchange.domain.model.ConvertedAmount
import com.sharmadhiraj.mycurrencyexchange.domain.model.Currency
import javax.inject.Inject

class ConvertCurrencyUseCase @Inject constructor() {

    /** Converts [amount] of [fromCode] into every currency in [currencies]. */
    operator fun invoke(
        amount: Double,
        fromCode: String,
        currencies: List<Currency>,
    ): List<ConvertedAmount> {
        val fromRate = currencies.firstOrNull { it.code == fromCode }?.rate ?: return emptyList()
        return currencies.map { ConvertedAmount(it.code, it.name, amount * it.rate / fromRate) }
    }
}
