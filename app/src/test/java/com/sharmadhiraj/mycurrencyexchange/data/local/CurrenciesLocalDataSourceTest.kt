package com.sharmadhiraj.mycurrencyexchange.data.local

import com.sharmadhiraj.mycurrencyexchange.data.local.dao.CurrencyDao
import com.sharmadhiraj.mycurrencyexchange.data.local.entity.CurrencyEntity
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class CurrenciesLocalDataSourceTest {

    private val currencyDao: CurrencyDao = mockk()
    private val localDataSource = CurrenciesLocalDataSource(currencyDao)

    @Test
    fun `saveCurrencies should insert data into the database`() = runBlocking {
        // Given
        val currenciesEntity = createCurrenciesEntity()

        // Stubbing the behavior of the DAO
        coEvery { currencyDao.saveCurrencies(any()) } returns Unit
        coEvery { currencyDao.getCurrencies() } returns currenciesEntity

        // When
        localDataSource.saveCurrencies(currenciesEntity)

        // Then
        val storedEntity = currencyDao.getCurrencies()
        assertNotNull(storedEntity)
        assertEquals(currenciesEntity.size, storedEntity!!.size)
        assertEquals(currenciesEntity.first().code, storedEntity.first().code)
        assertEquals(currenciesEntity.first().name, storedEntity.first().name)
        assertEquals(currenciesEntity.first().rate, storedEntity.first().rate, 0.005)
    }

    @Test
    fun `getCurrencies should return data from the database`() = runBlocking {
        // Given
        val currenciesEntity = createCurrenciesEntity()

        // Stubbing the behavior of the DAO
        coEvery { currencyDao.getCurrencies() } returns currenciesEntity

        // When
        val retrievedEntity = localDataSource.getCurrencies()

        // Then
        assertNotNull(retrievedEntity)
        assertEquals(currenciesEntity.size, retrievedEntity!!.size)
        assertEquals(currenciesEntity.first().code, retrievedEntity.first().code)
        assertEquals(currenciesEntity.first().name, retrievedEntity.first().name)
        assertEquals(currenciesEntity.first().rate, retrievedEntity.first().rate, 0.005)
    }

    @After
    fun tearDown() {
        clearAllMocks()
        unmockkAll()
    }

    private fun createCurrenciesEntity(): List<CurrencyEntity> {
        return listOf(
            CurrencyEntity("USD", "United States Dollar", 1.0),
            CurrencyEntity("EUR", "Euro", 1.5),
            CurrencyEntity("GBP", "British Pound Sterling", 1.2)
        )
    }
}
