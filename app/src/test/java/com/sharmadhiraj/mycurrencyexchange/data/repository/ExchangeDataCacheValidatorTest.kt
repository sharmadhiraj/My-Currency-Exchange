package com.sharmadhiraj.mycurrencyexchange.data.repository

import android.content.SharedPreferences
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.util.concurrent.TimeUnit

class ExchangeDataCacheValidatorTest {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var exchangeDataCacheValidator: ExchangeDataCacheValidator

    @Before
    fun setUp() {
        sharedPreferences = mockk(relaxed = true)
        exchangeDataCacheValidator = ExchangeDataCacheValidator(sharedPreferences)
    }

    @Test
    fun `isCacheValid should return true if cache is valid`() = runBlocking {
        // Given
        coEvery {
            sharedPreferences.getLong(
                any(),
                any()
            )
        } returns System.currentTimeMillis() - TimeUnit.MINUTES.toMillis(29)

        // When
        val result = exchangeDataCacheValidator.isCacheValid()

        // Then
        assert(result)
        coVerify { sharedPreferences.getLong(any(), any()) }
    }

    @Test
    fun `isCacheValid should return false if cache is not valid`() = runBlocking {
        // Given
        coEvery {
            sharedPreferences.getLong(
                any(),
                any()
            )
        } returns System.currentTimeMillis() - TimeUnit.MINUTES.toMillis(31)

        // When
        val result = exchangeDataCacheValidator.isCacheValid()

        // Then
        assert(!result)
        coVerify { sharedPreferences.getLong(any(), any()) }
    }

    @Test
    fun `updateLastRefreshTime should update the last refresh time in SharedPreferences`() =
        runBlocking {
            // Given
            coEvery {
                sharedPreferences.edit().putLong(any(), any())
            } returns sharedPreferences.edit()

            // When
            exchangeDataCacheValidator.updateLastRefreshTime()

            // Then
            coVerify { sharedPreferences.edit().putLong(any(), any()) }
        }

    @After
    fun tearDown() {
        clearAllMocks()
        unmockkAll()
    }
}
