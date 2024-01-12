package com.sharmadhiraj.mycurrencyexchange.domain.model

data class ExchangeRates(
    val base: String,
    val timestamp: Long,
    val rates: Map<String, Double>
)