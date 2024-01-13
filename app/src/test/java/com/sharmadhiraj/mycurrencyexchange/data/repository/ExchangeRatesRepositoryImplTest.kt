package com.sharmadhiraj.mycurrencyexchange.data.repository

import com.sharmadhiraj.mycurrencyexchange.data.local.ExchangeRatesLocalDataSource
import com.sharmadhiraj.mycurrencyexchange.data.local.dao.ExchangeRatesDao
import com.sharmadhiraj.mycurrencyexchange.data.local.entity.ExchangeRatesEntity
import com.sharmadhiraj.mycurrencyexchange.data.remote.ExchangeRatesRemoteDataSource
import com.sharmadhiraj.mycurrencyexchange.data.remote.api.ApiException
import com.sharmadhiraj.mycurrencyexchange.domain.exception.ExchangeRatesFetchException
import com.sharmadhiraj.mycurrencyexchange.domain.model.ExchangeRates
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Test
import java.util.Date

class ExchangeRatesRepositoryImplTest {

    private lateinit var repository: ExchangeRatesRepositoryImpl
    private lateinit var remoteDataSource: ExchangeRatesRemoteDataSource
    private lateinit var localDataSource: ExchangeRatesLocalDataSource
    private val exchangeRatesDao: ExchangeRatesDao = mockk()

    @Before
    fun setup() {
        remoteDataSource = mockk()
        localDataSource = ExchangeRatesLocalDataSource(exchangeRatesDao)
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
        coVerify(inverse = true) { remoteDataSource.getExchangeRates() }
        assertEquals(localData.timestamp, result.timestamp)
    }

    @Test
    fun `getExchangeRates - when local data is expired - should return remote data and save to local`() {
        // Given
        val localData = createMockExchangeRatesEntity(isExpired = true)
        val remoteData = createMockExchangeRates()
        coEvery { exchangeRatesDao.saveExchangeRates(any()) } returns Unit
        coEvery { localDataSource.getExchangeRates() } returns localData
        coEvery { remoteDataSource.getExchangeRates() } returns remoteData

        // When
        val result = runBlocking { repository.getExchangeRates() }

        // Then
        coVerify { remoteDataSource.getExchangeRates() }
        coVerify { exchangeRatesDao.saveExchangeRates(any()) }
        assertNotEquals(localData.timestamp, result.timestamp)
    }

    @Test
    fun `getExchangeRates - when remote data fail and local data is available (expired) - should return local data`() {
        // Given
        val localData = createMockExchangeRatesEntity(isExpired = true)
        coEvery { localDataSource.getExchangeRates() } returns localData
        coEvery { remoteDataSource.getExchangeRates() } throws ApiException("Error")

        // When
        val result = runBlocking { repository.getExchangeRates() }

        // Then
        coVerify { remoteDataSource.getExchangeRates() }
        coVerify { localDataSource.getExchangeRates() }
        assertEquals(localData.timestamp, result.timestamp)
    }

    @Test(expected = ExchangeRatesFetchException::class)
    fun `getExchangeRates - when both local and remote data fail - should throw exception`() {
        // Given
        coEvery { localDataSource.getExchangeRates() } returns null
        coEvery { remoteDataSource.getExchangeRates() } throws ApiException("Error")

        // When
        runBlocking { repository.getExchangeRates() }

        // Then
        // Exception will be thrown, function annotation @Test(expected = ExchangeRatesFetchException::class)
        coVerify { remoteDataSource.getExchangeRates() }
        coVerify { localDataSource.getExchangeRates() }
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    private fun createMockExchangeRatesEntity(isExpired: Boolean = false): ExchangeRatesEntity {
        return ExchangeRatesEntity(
            "USD",
            System.currentTimeMillis() / 1000 - (if (isExpired) 31 * 60 else 0),
            mapOf("EUR" to 1.5, "GBP" to 1.2),
            updatedAt = Date(System.currentTimeMillis() - (if (isExpired) 31 * 60 * 1000 else 0))
        )
    }


    private fun createMockExchangeRates(): ExchangeRates {
        return ExchangeRates(
            "USD",
            System.currentTimeMillis() / 1000,
            mapOf("EUR" to 1.5, "GBP" to 1.2)
        )
    }
}
