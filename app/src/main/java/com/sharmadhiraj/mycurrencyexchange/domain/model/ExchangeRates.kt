package com.sharmadhiraj.mycurrencyexchange.domain.model

/**
 * @property lastUpdatedMillis when the rates were last fetched from the network, if known.
 * @property isStale true when a refresh failed and these are older cached rates.
 */
data class ExchangeRates(
    val currencies: List<Currency>,
    val lastUpdatedMillis: Long?,
    val isStale: Boolean = false,
)
