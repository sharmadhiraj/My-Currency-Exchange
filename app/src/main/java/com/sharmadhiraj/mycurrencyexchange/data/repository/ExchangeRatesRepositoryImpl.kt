package com.sharmadhiraj.mycurrencyexchange.data.repository

import com.sharmadhiraj.mycurrencyexchange.data.local.ExchangeRatesLocalDataSource
import com.sharmadhiraj.mycurrencyexchange.data.local.entity.ExchangeRatesEntity
import com.sharmadhiraj.mycurrencyexchange.data.remote.ExchangeRatesRemoteDataSource
import com.sharmadhiraj.mycurrencyexchange.data.remote.api.ApiException
import com.sharmadhiraj.mycurrencyexchange.domain.exception.ExchangeRatesFetchException
import com.sharmadhiraj.mycurrencyexchange.domain.model.ExchangeRates
import com.sharmadhiraj.mycurrencyexchange.domain.repository.ExchangeRatesRepository
import com.sharmadhiraj.mycurrencyexchange.util.CommonUtil
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class ExchangeRatesRepositoryImpl @Inject constructor(
    private val remoteDataSource: ExchangeRatesRemoteDataSource,
    private val localDataSource: ExchangeRatesLocalDataSource
) : ExchangeRatesRepository {

    override suspend fun getExchangeRates(): ExchangeRates {
        val localData: ExchangeRatesEntity? =
            localDataSource.getExchangeRates()
        try {
            return if (localData != null && !CommonUtil.isExpiredData(localData.updatedAt)) {
                Mapper.mapExchangeRatesEntityToDomain(localData)
            } else {
                val remoteData: ExchangeRates = remoteDataSource.getExchangeRates()
                localDataSource.saveExchangeRates(Mapper.mapExchangeRatesDomainToEntity(remoteData))
                remoteData
            }
        } catch (e: ApiException) {
            if (localData != null) {
                return Mapper.mapExchangeRatesEntityToDomain(localData)
            }
            throw ExchangeRatesFetchException(e.message ?: "Unknown error", e.cause)
        }
    }

}
