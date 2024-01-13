package com.sharmadhiraj.mycurrencyexchange.data.repository

import com.sharmadhiraj.mycurrencyexchange.data.local.entity.CurrencyEntity
import com.sharmadhiraj.mycurrencyexchange.domain.model.Currency
import io.mockk.clearAllMocks
import io.mockk.unmockkAll
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Test

class MapperTest {

    @Test
    fun `mapCurrencyEntityToDomain should map List of CurrencyEntity to List of Currency`() {
        // Given
        val currencyEntities = listOf(
            CurrencyEntity("USD", "United States Dollar", 1.0),
            CurrencyEntity("EUR", "Euro", 1.5)
        )

        // When
        val result = Mapper.mapCurrencyEntityToDomain(currencyEntities)

        // Then
        val expected = listOf(
            Currency("USD", "United States Dollar", 1.0),
            Currency("EUR", "Euro", 1.5)
        )
        assertEquals(expected, result)
    }

    @Test
    fun `mapCurrencyDomainToEntity should map List of Currency to List of CurrencyEntity`() {
        // Given
        val currencies = listOf(
            Currency("USD", "United States Dollar", 1.0),
            Currency("EUR", "Euro", 1.5)
        )

        // When
        val result = Mapper.mapCurrencyDomainToEntity(currencies)

        // Then
        val expected = listOf(
            CurrencyEntity("USD", "United States Dollar", 1.0),
            CurrencyEntity("EUR", "Euro", 1.5)
        )
        assertEquals(expected, result)
    }

    @After
    fun tearDown() {
        clearAllMocks()
        unmockkAll()
    }
}
