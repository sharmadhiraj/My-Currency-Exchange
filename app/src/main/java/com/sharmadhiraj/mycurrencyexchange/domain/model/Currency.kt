package com.sharmadhiraj.mycurrencyexchange.domain.model

/** A currency with its rate relative to the API base currency (USD). */
data class Currency(
    val code: String,
    val name: String,
    val rate: Double,
)
