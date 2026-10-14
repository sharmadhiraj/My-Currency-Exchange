package com.sharmadhiraj.mycurrencyexchange.data.repository

import com.sharmadhiraj.mycurrencyexchange.data.local.CurrenciesLocalDataSource
import com.sharmadhiraj.mycurrencyexchange.data.mapper.toDomainModels
import com.sharmadhiraj.mycurrencyexchange.data.mapper.toEntities
import com.sharmadhiraj.mycurrencyexchange.data.remote.CurrenciesRemoteDataSource
import com.sharmadhiraj.mycurrencyexchange.data.remote.api.ApiException
import com.sharmadhiraj.mycurrencyexchange.domain.exception.ExchangeRatesUnavailableException
import com.sharmadhiraj.mycurrencyexchange.domain.model.ExchangeRates
import com.sharmadhiraj.mycurrencyexchange.domain.repository.ExchangeRatesRepository
import java.io.IOException
import javax.inject.Inject

/**
 * Serves rates from Room while they are fresh, otherwise from the network.
 * If the network fails, the last cached rates are returned and flagged as stale.
 */
class CurrenciesRepositoryImpl @Inject constructor(
    private val remoteDataSource: CurrenciesRemoteDataSource,
    private val localDataSource: CurrenciesLocalDataSource,
    private val cachePolicy: RatesCachePolicy,
) : ExchangeRatesRepository {

    override suspend fun getExchangeRates(forceRefresh: Boolean): ExchangeRates {
        val cached = localDataSource.getCurrencies()
        if (!forceRefresh && cached.isNotEmpty() && cachePolicy.isFresh()) {
            return ExchangeRates(cached.toDomainModels(), cachePolicy.lastRefreshMillis())
        }

        return try {
            val remote = remoteDataSource.getCurrencies()
            localDataSource.replaceCurrencies(remote.toEntities())
            cachePolicy.markRefreshed()
            ExchangeRates(remote, cachePolicy.lastRefreshMillis())
        } catch (e: ApiException) {
            if (cached.isEmpty()) {
                throw ExchangeRatesUnavailableException(
                    message = e.message ?: "Unknown error",
                    isNetworkError = e.cause is IOException,
                    cause = e,
                )
            }
            ExchangeRates(cached.toDomainModels(), cachePolicy.lastRefreshMillis(), isStale = true)
        }
    }
}
