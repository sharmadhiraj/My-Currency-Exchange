package com.sharmadhiraj.mycurrencyexchange.domain.repository

import com.sharmadhiraj.mycurrencyexchange.domain.model.ExchangeRates

interface ExchangeRatesRepository {

    /** @throws com.sharmadhiraj.mycurrencyexchange.domain.exception.ExchangeRatesUnavailableException when nothing is cached and the network fails. */
    suspend fun getExchangeRates(forceRefresh: Boolean = false): ExchangeRates
}
