package com.sharmadhiraj.mycurrencyexchange.data.local

import com.sharmadhiraj.mycurrencyexchange.data.local.dao.CurrencyDao
import com.sharmadhiraj.mycurrencyexchange.data.local.entity.CurrencyEntity
import javax.inject.Inject

class CurrenciesLocalDataSource @Inject constructor(private val currencyDao: CurrencyDao) {

    suspend fun getCurrencies(): List<CurrencyEntity> = currencyDao.getCurrencies()

    suspend fun replaceCurrencies(currencies: List<CurrencyEntity>) {
        currencyDao.replaceCurrencies(currencies)
    }
}
