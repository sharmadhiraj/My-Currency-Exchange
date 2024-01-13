package com.sharmadhiraj.mycurrencyexchange.data.local

import com.google.gson.Gson
import io.mockk.clearAllMocks
import io.mockk.unmockkAll
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.Date

class ConvertersTest {

    private val converters = Converters()

    @Test
    fun `fromString converts JSON to Map correctly`() {
        // Given
        val json = """{"USD": 1.0, "EUR": 0.8, "GBP": 0.7}"""

        // When
        val result = converters.fromString(json)

        // Then
        val expected = mapOf("USD" to 1.0, "EUR" to 0.8, "GBP" to 0.7)
        assertEquals(expected, result)
    }

    @Test
    fun `toString converts Map to JSON correctly`() {
        // Given
        val map = mapOf("USD" to 1.0, "EUR" to 0.8, "GBP" to 0.7)

        // When
        val result = converters.toString(map)

        // Then
        val expectedJson = Gson().toJson(map)
        assertEquals(expectedJson, result)
    }

    @Test
    fun `fromTimestamp converts timestamp to Date correctly`() {
        // Given
        val timestamp = 123456789L

        // When
        val result = converters.fromTimestamp(timestamp)

        // Then
        val expectedDate = Date(timestamp)
        assertEquals(expectedDate, result)
    }

    @Test
    fun `dateToTimestamp converts Date to timestamp correctly`() {
        // Given
        val date = Date(123456789L)

        // When
        val result = converters.dateToTimestamp(date)

        // Then
        val expectedTimestamp = date.time
        assertEquals(expectedTimestamp, result)
    }

    @After
    fun tearDown() {
        clearAllMocks()
        unmockkAll()
    }
}
