package com.sharmadhiraj.mycurrencyexchange.data.repository

import com.sharmadhiraj.mycurrencyexchange.data.local.entity.CurrencyEntity
import com.sharmadhiraj.mycurrencyexchange.data.local.source.CurrenciesLocalDataSource
import com.sharmadhiraj.mycurrencyexchange.data.remote.CurrenciesRemoteDataSource
import com.sharmadhiraj.mycurrencyexchange.data.remote.api.ApiException
import com.sharmadhiraj.mycurrencyexchange.domain.exception.ExchangeRatesFetchException
import com.sharmadhiraj.mycurrencyexchange.domain.model.Currency
import com.sharmadhiraj.mycurrencyexchange.domain.repository.ExchangeRatesRepository
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class CurrenciesRepositoryImpl @Inject constructor(
    private val remoteDataSource: CurrenciesRemoteDataSource,
    private val localDataSource: CurrenciesLocalDataSource
) : ExchangeRatesRepository {

    override suspend fun getCurrencies(): List<Currency> {
        val localData: List<CurrencyEntity>? = localDataSource.getCurrencies()
        try {
            return if (!localData.isNullOrEmpty()) {
                localData.map { e -> Mapper.mapExchangeRatesEntityToDomain(e) }
            } else {
                val remoteData: List<Currency> = remoteDataSource.getCurrenciesWithExchangeRate()
                localDataSource.saveCurrencies(remoteData.map { e ->
                    Mapper.mapExchangeRatesDomainToEntity(e)
                })
                remoteData
            }
        } catch (e: ApiException) {
            if (!localData.isNullOrEmpty()) {
                return localData.map { element -> Mapper.mapExchangeRatesEntityToDomain(element) }
            }
            throw ExchangeRatesFetchException(e.message ?: "Unknown error", e.cause)
        }
    }

}
