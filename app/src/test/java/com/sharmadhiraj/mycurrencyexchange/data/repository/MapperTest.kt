package com.sharmadhiraj.mycurrencyexchange.data.repository

import com.sharmadhiraj.mycurrencyexchange.data.local.entity.ExchangeRatesEntity
import com.sharmadhiraj.mycurrencyexchange.domain.model.ExchangeRates
import org.junit.Assert.assertEquals
import org.junit.Test

class MapperTest {

    @Test
    fun `mapExchangeRatesEntityToDomain should map correctly`() {
        // Given
        val entity = ExchangeRatesEntity("USD", 1705024800, mapOf("EUR" to 1.5, "GBP" to 1.2))

        // When
        val domain = Mapper.mapExchangeRatesEntityToDomain(entity)

        // Then
        assertEquals(entity.base, domain.base)
        assertEquals(entity.timestamp, domain.timestamp)
        assertEquals(entity.rates, domain.rates)
    }

    @Test
    fun `mapExchangeRatesDomainToEntity should map correctly`() {
        // Given
        val domain = ExchangeRates("USD", 1705024800, mapOf("EUR" to 1.5, "GBP" to 1.2))

        // When
        val entity = Mapper.mapExchangeRatesDomainToEntity(domain)

        // Then
        assertEquals(domain.base, entity.base)
        assertEquals(domain.timestamp, entity.timestamp)
        assertEquals(domain.rates, entity.rates)
    }
}