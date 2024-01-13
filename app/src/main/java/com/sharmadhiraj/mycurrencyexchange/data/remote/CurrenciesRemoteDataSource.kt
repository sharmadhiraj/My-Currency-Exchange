package com.sharmadhiraj.mycurrencyexchange.data.remote

import com.sharmadhiraj.mycurrencyexchange.BuildConfig
import com.sharmadhiraj.mycurrencyexchange.data.remote.api.ApiException
import com.sharmadhiraj.mycurrencyexchange.data.remote.api.ApiService
import com.sharmadhiraj.mycurrencyexchange.domain.model.Currency
import javax.inject.Inject


class CurrenciesRemoteDataSource @Inject constructor(private val apiService: ApiService) {

    private suspend fun getCurrencies(): Map<String, String> {
        try {
            val response = apiService.getCurrencies(BuildConfig.OPEN_EXCHANGE_RATES_APP_ID)
            if (response.isSuccessful) {
                val currencies = response.body()
                if (currencies == null) {
                    throw ApiException("Response null or empty")
                } else {
                    return currencies
                }
            } else {
                throw ApiException("Unsuccessful response status code ${response.code()}")
            }
        } catch (e: Exception) {
            throw ApiException("API error: ${e.message}", e)
        }
    }

    private suspend fun getExchangeRates(): Map<String, Double> {
        try {
            val response = apiService.getLatestExchangeRates(BuildConfig.OPEN_EXCHANGE_RATES_APP_ID)
            if (response.isSuccessful) {
                val exchangeRatesApiResponse = response.body()
                if (exchangeRatesApiResponse?.rates == null) {
                    throw ApiException("Response null or empty")
                } else {
                    return exchangeRatesApiResponse.rates
                }
            } else {
                throw ApiException("Unsuccessful response status code ${response.code()}")
            }
        } catch (e: Exception) {
            throw ApiException("API error: ${e.message}", e)
        }
    }

    suspend fun getCurrenciesWithExchangeRate(): List<Currency> {
        try {
            val currenciesWithExchangeRate = mutableListOf<Currency>()
            val exchangeRates = getExchangeRates()
            val currencies = getCurrencies()
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