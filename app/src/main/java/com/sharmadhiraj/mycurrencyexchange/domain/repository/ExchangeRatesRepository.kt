package com.sharmadhiraj.mycurrencyexchange.domain.repository

import com.sharmadhiraj.mycurrencyexchange.domain.model.Currency

interface ExchangeRatesRepository {
    suspend fun getCurrencies(): List<Currency>
}