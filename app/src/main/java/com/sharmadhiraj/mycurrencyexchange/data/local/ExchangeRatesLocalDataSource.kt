package com.sharmadhiraj.mycurrencyexchange.data.local

import com.sharmadhiraj.mycurrencyexchange.data.local.dao.ExchangeRatesDao
import com.sharmadhiraj.mycurrencyexchange.data.local.entity.ExchangeRatesEntity

class ExchangeRatesLocalDataSource(private val exchangeRatesDao: ExchangeRatesDao) {

    suspend fun getExchangeRates(): ExchangeRatesEntity? {
        return exchangeRatesDao.getExchangeRates()
    }

    suspend fun saveExchangeRates(entity: ExchangeRatesEntity) {
        exchangeRatesDao.saveExchangeRates(entity)
    }
}