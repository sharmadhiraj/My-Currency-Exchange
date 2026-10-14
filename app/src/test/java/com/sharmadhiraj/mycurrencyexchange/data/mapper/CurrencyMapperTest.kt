package com.sharmadhiraj.mycurrencyexchange.data.mapper

import com.sharmadhiraj.mycurrencyexchange.data.local.entity.CurrencyEntity
import com.sharmadhiraj.mycurrencyexchange.domain.model.Currency
import org.junit.Assert.assertEquals
import org.junit.Test

class CurrencyMapperTest {

    private val entities = listOf(
        CurrencyEntity("USD", "United States Dollar", 1.0),
        CurrencyEntity("EUR", "Euro", 1.5),
    )
    private val currencies = listOf(
        Currency("USD", "United States Dollar", 1.0),
        Currency("EUR", "Euro", 1.5),
    )

    @Test
    fun `entities map to domain models`() {
        assertEquals(currencies, entities.toDomainModels())
    }

    @Test
    fun `domain models map to entities`() {
        assertEquals(entities, currencies.toEntities())
    }
}
