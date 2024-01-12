package com.sharmadhiraj.mycurrencyexchange.data.repository

import com.sharmadhiraj.mycurrencyexchange.data.local.ExchangeRatesLocalDataSource
import com.sharmadhiraj.mycurrencyexchange.data.local.entity.ExchangeRatesEntity
import com.sharmadhiraj.mycurrencyexchange.data.remote.ExchangeRatesRemoteDataSource
import com.sharmadhiraj.mycurrencyexchange.data.remote.api.ApiException
import com.sharmadhiraj.mycurrencyexchange.domain.exception.ExchangeRatesFetchException
import com.sharmadhiraj.mycurrencyexchange.domain.model.ExchangeRates
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Test

class ExchangeRatesRepositoryImplTest {

    private lateinit var repository: ExchangeRatesRepositoryImpl
    private lateinit var remoteDataSource: ExchangeRatesRemoteDataSource
    private lateinit var localDataSource: ExchangeRatesLocalDataSource

    @Before
    fun setup() {
        remoteDataSource = mockk()
        localDataSource = mockk()
        repository = ExchangeRatesRepositoryImpl(remoteDataSource, localDataSource)
    }

    @Test
    fun `getExchangeRates - when local data is not expired - should return local data`() {
        // Given
        val localData = createMockExchangeRatesEntity(isExpired = false)
        coEvery { localDataSource.getExchangeRates() } returns localData

        // When
        val result = runBlocking { repository.getExchangeRates() }

        // Then
        assertEquals(localData.timestamp, result.timestamp)
    }

    @Test
    fun `getExchangeRates - when local data is expired - should return remote data and save to local`() {
        // Given
        val localData = createMockExchangeRatesEntity(isExpired = true)
        val remoteData = createMockExchangeRates()
        coEvery { localDataSource.getExchangeRates() } returns localData
        coEvery { remoteDataSource.getExchangeRates() } returns remoteData

        // When
        val result = runBlocking { repository.getExchangeRates() }

        // Then
        assertNotEquals(localData.timestamp, result.timestamp)
    }

    @Test(expected = ExchangeRatesFetchException::class)
    fun `getExchangeRates - when both local and remote data fail - should throw exception`() {
        // Arrange
        coEvery { localDataSource.getExchangeRates() } returns null
        coEvery { remoteDataSource.getExchangeRates() } throws ApiException("Error")

        // Act
        runBlocking { repository.getExchangeRates() }

        // Exception will be thrown, and the test will pass if the exception is caught correctly
    }

    private fun createMockExchangeRatesEntity(isExpired: Boolean): ExchangeRatesEntity {
        val timestamp = if (isExpired) {
            System.currentTimeMillis() - (31 * 60 * 1000)
        } else {
            System.currentTimeMillis()
        }
        return ExchangeRatesEntity("USD", timestamp / 1000, mapOf("EUR" to 1.5, "GBP" to 1.2))
    }


    private fun createMockExchangeRates(): ExchangeRates {
        return ExchangeRates(
            "USD",
            System.currentTimeMillis() / 1000,
            mapOf("EUR" to 1.5, "GBP" to 1.2)
        )
    }
}
