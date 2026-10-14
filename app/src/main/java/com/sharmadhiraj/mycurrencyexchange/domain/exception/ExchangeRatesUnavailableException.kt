package com.sharmadhiraj.mycurrencyexchange.domain.exception

class ExchangeRatesUnavailableException(
    message: String,
    val isNetworkError: Boolean = false,
    cause: Throwable? = null,
) : Exception(message, cause)
