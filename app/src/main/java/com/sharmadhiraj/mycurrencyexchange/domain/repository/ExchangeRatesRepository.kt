package com.sharmadhiraj.mycurrencyexchange.domain.repository

import com.sharmadhiraj.mycurrencyexchange.domain.model.ExchangeRates

interface ExchangeRatesRepository {
    suspend fun getExchangeRates(): ExchangeRates
}