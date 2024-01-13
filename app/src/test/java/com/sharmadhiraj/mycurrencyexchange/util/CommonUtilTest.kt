package com.sharmadhiraj.mycurrencyexchange.util


import com.sharmadhiraj.mycurrencyexchange.domain.model.Currency
import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import io.mockk.unmockkAll
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.Locale

class CommonUtilTest {

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `formatDateTime should format timestamp correctly`() {
        // Given
        val timestamp = 1705024800L
        val expectedFormat =
            SimpleDateFormat("HH:mm:ss, dd MMMM yyyy", Locale.ENGLISH).format(timestamp * 1000)

        // When
        val formattedDateTime = CommonUtil.formatDateTime(timestamp)

        // Then
        assertEquals(expectedFormat, formattedDateTime)
    }

    @Test
    fun `stringToDouble should convert valid string to double`() {
        // Given
        val validString = "10.5"

        // When
        val result = CommonUtil.stringToDouble(validString)

        // Then
        assertEquals(10.5, result, 0.0)
    }

    @Test
    fun `stringToDouble should return 0 for invalid string`() {
        // Given
        val invalidString = "abc"

        // When
        val result = CommonUtil.stringToDouble(invalidString)

        // Then
        assertEquals(0.0, result, 0.0)
    }

    @Test
    fun `convertCurrencies should convert currency correctly #1`() {
        // Given
        val amount = 10.0
        val selectedCurrency = "EUR"
        val exchangeRates = listOf(
            Currency("USD", "United States Dollar", 1.5),
            Currency("GBP", "British Pound Sterling", 1.2),
            Currency("EUR", "Euro", 1.0)
        )

        // When
        val convertedCurrencies =
            CommonUtil.convertCurrencies(amount, selectedCurrency, exchangeRates)

        // Then
        val expectedConvertedCurrencies = listOf(
            Currency("USD", "United States Dollar", 15.0),
            Currency("GBP", "British Pound Sterling", 12.0),
            Currency("EUR", "Euro", 10.0)
        )
        assertEquals(expectedConvertedCurrencies, convertedCurrencies)
    }

    @Test
    fun `convertCurrencies should convert currency correctly #2`() {
        // Given
        val amount = 145.50
        val selectedCurrency = "EUR"
        val exchangeRates = listOf(
            Currency("USD", "United States Dollar", 1.5),
            Currency("GBP", "British Pound Sterling", 1.2),
            Currency("EUR", "Euro", 1.0)
        )

        // When
        val convertedCurrencies =
            CommonUtil.convertCurrencies(amount, selectedCurrency, exchangeRates)

        // Then
        val expectedConvertedCurrencies = listOf(
            Currency("USD", "United States Dollar", 218.25),
            Currency("GBP", "British Pound Sterling", 174.6),
            Currency("EUR", "Euro", 145.5)
        )
        assertEquals(expectedConvertedCurrencies, convertedCurrencies)
    }

    @After
    fun tearDown() {
        clearAllMocks()
        unmockkAll()
    }

}
