package com.sharmadhiraj.mycurrencyexchange.data.local.source

import com.sharmadhiraj.mycurrencyexchange.data.local.dao.CurrencyDao
import com.sharmadhiraj.mycurrencyexchange.data.local.entity.CurrencyEntity
import javax.inject.Inject

class CurrenciesLocalDataSource @Inject constructor(private val currencyDao: CurrencyDao) {

    suspend fun getCurrencies(): List<CurrencyEntity>? {
        return currencyDao.getCurrencies()
    }

    suspend fun saveCurrencies(currencies: List<CurrencyEntity>) {
        currencyDao.saveCurrencies(currencies)
    }
}