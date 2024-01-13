package com.sharmadhiraj.mycurrencyexchange.util

import io.mockk.clearAllMocks
import io.mockk.unmockkAll
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CommonUtilTest {

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
    fun `convertCurrency should convert currency correctly #1`() {
        // Given
        val amount = 10.0
        val selectedCurrency = "EUR"
        val exchangeRates = mapOf("USD" to 1.5, "GBP" to 1.2, "EUR" to 1.0)

        // When
        val convertedAmounts = CommonUtil.convertCurrency(amount, selectedCurrency, exchangeRates)

        // Then
        val expectedConvertedAmounts = mapOf("USD" to 15.0, "GBP" to 12.0, "EUR" to 10.0)
        assertEquals(expectedConvertedAmounts, convertedAmounts)
    }

    @Test
    fun `convertCurrency should convert currency correctly #2`() {
        // Given
        val amount = 145.50
        val selectedCurrency = "EUR"
        val exchangeRates = mapOf("USD" to 1.5, "GBP" to 1.2, "EUR" to 1.0)

        // When
        val convertedAmounts = CommonUtil.convertCurrency(amount, selectedCurrency, exchangeRates)

        // Then
        val expectedConvertedAmounts = mapOf("USD" to 218.25, "GBP" to 174.6, "EUR" to 145.5)
        assertEquals(expectedConvertedAmounts, convertedAmounts)
    }

    @Test
    fun `isExpiredData should return false for recent date`() {
        // Given
        val updatedAt = Date()

        // When
        val result = CommonUtil.isExpiredData(updatedAt)

        // Then
        assertFalse(result)
    }

    @Test
    fun `isExpiredData should return true for date more than 30 minutes ago`() {
        // Given
        val thirtyMinutesAgo = Date(Date().time - 30 * 60 * 1000)
        val updatedAt =
            Date(thirtyMinutesAgo.time - 1000) // Subtracting 1 second to make it more than 30 minutes

        // When
        val result = CommonUtil.isExpiredData(updatedAt)

        // Then
        assertTrue(result)
    }

    @After
    fun tearDown() {
        clearAllMocks()
        unmockkAll()
    }

}
