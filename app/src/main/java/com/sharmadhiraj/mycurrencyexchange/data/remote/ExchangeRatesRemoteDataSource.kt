package com.sharmadhiraj.mycurrencyexchange.data.remote

import com.sharmadhiraj.mycurrencyexchange.BuildConfig
import com.sharmadhiraj.mycurrencyexchange.data.remote.api.ApiException
import com.sharmadhiraj.mycurrencyexchange.data.remote.api.ApiService
import com.sharmadhiraj.mycurrencyexchange.domain.model.ExchangeRates

class ExchangeRatesRemoteDataSource(private val apiService: ApiService) {

    suspend fun getExchangeRates(): ExchangeRates {
        try {
            val response =
                apiService.getLatestExchangeRates(BuildConfig.OPEN_EXCHANGE_RATES_APP_ID)

            if (response.isSuccessful) {
                val exchangeRatesApiResponse = response.body()
                if (exchangeRatesApiResponse?.base == null || exchangeRatesApiResponse.timestamp == null || exchangeRatesApiResponse.rates == null) {
                    throw ApiException("Response does not have required data.")
                } else {
                    return ExchangeRates(
                        base = exchangeRatesApiResponse.base,
                        timestamp = exchangeRatesApiResponse.timestamp,
                        rates = exchangeRatesApiResponse.rates
                    )
                }
            } else {
                throw ApiException("API error: ${response.code()}")
            }
        } catch (e: Exception) {
            throw ApiException("Network error: ${e.message}", e)
        }
    }
}