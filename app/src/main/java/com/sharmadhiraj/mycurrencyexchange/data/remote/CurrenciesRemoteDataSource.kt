package com.sharmadhiraj.mycurrencyexchange.data.remote

import com.sharmadhiraj.mycurrencyexchange.BuildConfig
import com.sharmadhiraj.mycurrencyexchange.data.remote.api.ApiClient
import com.sharmadhiraj.mycurrencyexchange.data.remote.api.ApiException
import com.sharmadhiraj.mycurrencyexchange.data.remote.api.ApiService
import com.sharmadhiraj.mycurrencyexchange.domain.model.Currency
import javax.inject.Inject


class CurrenciesRemoteDataSource @Inject constructor(private val apiService: ApiService) {

    suspend fun getCurrencies(): List<Currency> {
        try {
            val currenciesWithExchangeRate = mutableListOf<Currency>()
            val exchangeRates = ApiClient.fetchData {
                apiService.getLatestExchangeRates(BuildConfig.OPEN_EXCHANGE_RATES_APP_ID)
            }.rates!!
            val currencies = ApiClient.fetchData {
                apiService.getCurrencies(BuildConfig.OPEN_EXCHANGE_RATES_APP_ID)
            }!!
            for (currency in currencies) {
                currenciesWithExchangeRate.add(
                    Currency(
                        currency.key,
                        currency.value,
                        exchangeRates[currency.key] ?: 1.0
                    )
                )
            }

            return currenciesWithExchangeRate
        } catch (e: Exception) {
            throw ApiException(e.message ?: "", e)
        }
    }
}