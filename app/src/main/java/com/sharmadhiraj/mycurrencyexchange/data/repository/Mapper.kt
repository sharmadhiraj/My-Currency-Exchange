package com.sharmadhiraj.mycurrencyexchange.data.repository

import com.sharmadhiraj.mycurrencyexchange.data.local.entity.CurrencyEntity
import com.sharmadhiraj.mycurrencyexchange.domain.model.Currency

object Mapper {
    fun mapExchangeRatesEntityToDomain(entity: CurrencyEntity): Currency {
        return Currency(entity.code, entity.name, entity.rate)
    }

    fun mapExchangeRatesDomainToEntity(domain: Currency): CurrencyEntity {
        return CurrencyEntity(domain.code, domain.name, domain.rate)
    }
}