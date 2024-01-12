package com.sharmadhiraj.mycurrencyexchange.data.repository

import com.sharmadhiraj.mycurrencyexchange.data.local.ExchangeRatesLocalDataSource
import com.sharmadhiraj.mycurrencyexchange.data.local.entity.ExchangeRatesEntity
import com.sharmadhiraj.mycurrencyexchange.data.remote.ExchangeRatesRemoteDataSource
import com.sharmadhiraj.mycurrencyexchange.data.remote.api.ApiException
import com.sharmadhiraj.mycurrencyexchange.domain.exception.ExchangeRatesFetchException
import com.sharmadhiraj.mycurrencyexchange.domain.model.ExchangeRates
import com.sharmadhiraj.mycurrencyexchange.domain.repository.ExchangeRatesRepository

class ExchangeRatesRepositoryImpl(
    private val remoteDataSource: ExchangeRatesRemoteDataSource,
    private val localDataSource: ExchangeRatesLocalDataSource,
) : ExchangeRatesRepository {

    override suspend fun getExchangeRates(): ExchangeRates {
        val cachedData: ExchangeRatesEntity? =
            localDataSource.getExchangeRates()
        try {
            return if (cachedData != null && !isDataStale(cachedData.timestamp)) {
                mapExchangeRatesEntityToDomain(cachedData)
            } else {
                val remoteData: ExchangeRates = remoteDataSource.getExchangeRates()
                localDataSource.saveExchangeRates(mapExchangeRatesToEntity(remoteData))
                remoteData
            }
        } catch (e: ApiException) {
            if (cachedData != null) {
                mapExchangeRatesEntityToDomain(cachedData)
            }
            throw ExchangeRatesFetchException(e.message ?: "Error fetching exchange rates", e.cause)
        }
    }

    private fun isDataStale(timestamp: Long?): Boolean {
        if (timestamp == null) {
            return true
        }
        val currentTime = System.currentTimeMillis()
        val dataAge = currentTime - timestamp
        val staleThreshold = 30 * 60 * 1000 //30 minutes
        return dataAge > staleThreshold
    }

    private fun mapExchangeRatesEntityToDomain(entity: ExchangeRatesEntity): ExchangeRates {
        return ExchangeRates(entity.base, entity.timestamp, entity.rates)
    }

    private fun mapExchangeRatesToEntity(domain: ExchangeRates): ExchangeRatesEntity {
        return ExchangeRatesEntity(domain.base, domain.timestamp, domain.rates)
    }
}
