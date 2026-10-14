package com.sharmadhiraj.mycurrencyexchange.data.remote

import com.sharmadhiraj.mycurrencyexchange.data.remote.api.ApiException
import com.sharmadhiraj.mycurrencyexchange.data.remote.api.ApiService
import com.sharmadhiraj.mycurrencyexchange.data.remote.api.safeApiCall
import com.sharmadhiraj.mycurrencyexchange.di.OpenExchangeRatesAppId
import com.sharmadhiraj.mycurrencyexchange.domain.model.Currency
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class CurrenciesRemoteDataSource @Inject constructor(
    private val apiService: ApiService,
    @param:OpenExchangeRatesAppId private val appId: String,
) {

    /** Fetches rates and names in parallel and joins them. Currencies without a rate are dropped. */
    suspend fun getCurrencies(): List<Currency> = coroutineScope {
        val rates = async { safeApiCall { apiService.getLatestExchangeRates(appId) }.rates.orEmpty() }
        val names = async { safeApiCall { apiService.getCurrencies(appId) } }

        val rateByCode = rates.await()
        names.await()
            .mapNotNull { (code, name) -> rateByCode[code]?.let { Currency(code, name, it) } }
            .sortedBy { it.code }
            .ifEmpty { throw ApiException("No exchange rates returned") }
    }
}
