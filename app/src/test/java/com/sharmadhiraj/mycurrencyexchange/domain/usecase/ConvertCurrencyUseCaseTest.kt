package com.sharmadhiraj.mycurrencyexchange.domain.usecase

import com.sharmadhiraj.mycurrencyexchange.domain.model.ConvertedAmount
import com.sharmadhiraj.mycurrencyexchange.domain.model.Currency
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ConvertCurrencyUseCaseTest {

    private val convert = ConvertCurrencyUseCase()
    private val currencies = listOf(
        Currency("USD", "United States Dollar", 1.0),
        Currency("EUR", "Euro", 0.5),
        Currency("GBP", "British Pound Sterling", 0.25),
    )

    @Test
    fun `converts through the base rate`() {
        val result = convert(10.0, "EUR", currencies)

        assertEquals(
            listOf(
                ConvertedAmount("USD", "United States Dollar", 20.0),
                ConvertedAmount("EUR", "Euro", 10.0),
                ConvertedAmount("GBP", "British Pound Sterling", 5.0),
            ),
            result
        )
    }

    @Test
    fun `zero amount converts to zero everywhere`() {
        assertTrue(convert(0.0, "USD", currencies).all { it.amount == 0.0 })
    }

    @Test
    fun `unknown source currency yields no results`() {
        assertTrue(convert(10.0, "XXX", currencies).isEmpty())
    }
}
