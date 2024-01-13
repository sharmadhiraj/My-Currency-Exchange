package com.sharmadhiraj.mycurrencyexchange.data.repository

import com.sharmadhiraj.mycurrencyexchange.data.local.CurrenciesLocalDataSource
import com.sharmadhiraj.mycurrencyexchange.data.local.dao.CurrencyDao
import com.sharmadhiraj.mycurrencyexchange.data.local.entity.CurrencyEntity
import com.sharmadhiraj.mycurrencyexchange.data.remote.CurrenciesRemoteDataSource
import com.sharmadhiraj.mycurrencyexchange.data.remote.api.ApiException
import com.sharmadhiraj.mycurrencyexchange.domain.exception.ExchangeRatesFetchException
import com.sharmadhiraj.mycurrencyexchange.domain.model.Currency
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Test

class CurrenciesRepositoryImplTest {

    private lateinit var repository: CurrenciesRepositoryImpl
    private lateinit var remoteDataSource: CurrenciesRemoteDataSource
    private lateinit var localDataSource: CurrenciesLocalDataSource
    private lateinit var exchangeDataPreferences: ExchangeDataCacheValidator
    private val currencyDao: CurrencyDao = mockk()

    @Before
    fun setup() {
        remoteDataSource = mockk()
        exchangeDataPreferences = mockk()
        localDataSource = CurrenciesLocalDataSource(currencyDao)
        repository =
            CurrenciesRepositoryImpl(remoteDataSource, localDataSource, exchangeDataPreferences)
    }

    @Test
    fun `getCurrencies - when local data is not expired - should return local data`() {
        // Given
        val localData = createMockCurrencyEntities()
        coEvery { localDataSource.getCurrencies() } returns localData
        coEvery { exchangeDataPreferences.isCacheValid() } returns true

        // When
        val result = runBlocking { repository.getCurrencies() }

        // Then
        coVerify(inverse = true) { remoteDataSource.getCurrencies() }
        assertEquals(Mapper.mapCurrencyEntityToDomain(localData), result)
    }

    @Test
    fun `getCurrencies - when local data is expired - should return remote data and save to local`() {
        // Given
        val localData = createMockCurrencyEntities()
        val remoteData = createCurrencies()
        coEvery { currencyDao.saveCurrencies(any()) } returns Unit
        coEvery { localDataSource.getCurrencies() } returns localData
        coEvery { remoteDataSource.getCurrencies() } returns remoteData
        coEvery { exchangeDataPreferences.updateLastRefreshTime() } returns Unit
        coEvery { exchangeDataPreferences.isCacheValid() } returns false

        // When
        val result = runBlocking { repository.getCurrencies() }

        // Then
        coVerify { remoteDataSource.getCurrencies() }
        coVerify { currencyDao.saveCurrencies(any()) }
        assertNotEquals(Mapper.mapCurrencyEntityToDomain(localData), result)
    }

    @Test
    fun `getCurrencies - when remote data fail and local data is available (expired) - should return local data`() {
        // Given
        val localData = createMockCurrencyEntities()
        coEvery { localDataSource.getCurrencies() } returns localData
        coEvery { remoteDataSource.getCurrencies() } throws ApiException("Error")
        coEvery { exchangeDataPreferences.isCacheValid() } returns false

        // When
        val result = runBlocking { repository.getCurrencies() }

        // Then
        coVerify { remoteDataSource.getCurrencies() }
        coVerify { localDataSource.getCurrencies() }
        assertEquals(Mapper.mapCurrencyEntityToDomain(localData), result)
    }

    @Test(expected = ExchangeRatesFetchException::class)
    fun `getCurrencies - when both local and remote data fail - should throw exception`() {
        // Given
        coEvery { localDataSource.getCurrencies() } returns null
        coEvery { remoteDataSource.getCurrencies() } throws ApiException("Error")

        // When
        runBlocking { repository.getCurrencies() }

        // Then
        // Exception will be thrown, function annotation @Test(expected = ExchangeRatesFetchException::class)
        coVerify { remoteDataSource.getCurrencies() }
        coVerify { localDataSource.getCurrencies() }
    }

    @After
    fun tearDown() {
        clearAllMocks()
        unmockkAll()
    }

    private fun createMockCurrencyEntities(): List<CurrencyEntity> {
        return listOf(
            CurrencyEntity(
                "USD",
                "United States Dollar",
                1.2
            )
        )
    }


    private fun createCurrencies(): List<Currency> {
        return listOf(
            Currency(
                "USD",
                "United States Dollar",
                1.5
            )
        )
    }
}
