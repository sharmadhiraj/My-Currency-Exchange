package com.sharmadhiraj.mycurrencyexchange.data.repository

import com.sharmadhiraj.mycurrencyexchange.data.local.entity.CurrencyEntity
import com.sharmadhiraj.mycurrencyexchange.domain.model.Currency

object Mapper {
    fun mapCurrencyEntityToDomain(currencyEntities: List<CurrencyEntity>): List<Currency> {
        return currencyEntities.map { entity -> Currency(entity.code, entity.name, entity.rate) }
    }

    fun mapCurrencyDomainToEntity(currencies: List<Currency>): List<CurrencyEntity> {
        return currencies.map { domain -> CurrencyEntity(domain.code, domain.name, domain.rate) }
    }
}