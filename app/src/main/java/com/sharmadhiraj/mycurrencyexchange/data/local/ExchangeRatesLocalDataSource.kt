package com.sharmadhiraj.mycurrencyexchange.data.local

import com.sharmadhiraj.mycurrencyexchange.data.local.dao.ExchangeRatesDao
import com.sharmadhiraj.mycurrencyexchange.data.local.entity.ExchangeRatesEntity
import javax.inject.Inject

class ExchangeRatesLocalDataSource @Inject constructor(private val exchangeRatesDao: ExchangeRatesDao) {

    suspend fun getExchangeRates(): ExchangeRatesEntity? {
        return exchangeRatesDao.getExchangeRates()
    }

    suspend fun saveExchangeRates(entity: ExchangeRatesEntity) {
        exchangeRatesDao.saveExchangeRates(entity)
    }
}