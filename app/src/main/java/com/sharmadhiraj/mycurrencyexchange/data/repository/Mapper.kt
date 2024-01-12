package com.sharmadhiraj.mycurrencyexchange.data.repository

import com.sharmadhiraj.mycurrencyexchange.data.local.entity.ExchangeRatesEntity
import com.sharmadhiraj.mycurrencyexchange.domain.model.ExchangeRates

object Mapper {
    fun mapExchangeRatesEntityToDomain(entity: ExchangeRatesEntity): ExchangeRates {
        return ExchangeRates(entity.base, entity.timestamp, entity.rates)
    }

    fun mapExchangeRatesDomainToEntity(domain: ExchangeRates): ExchangeRatesEntity {
        return ExchangeRatesEntity(domain.base, domain.timestamp, domain.rates)
    }
}