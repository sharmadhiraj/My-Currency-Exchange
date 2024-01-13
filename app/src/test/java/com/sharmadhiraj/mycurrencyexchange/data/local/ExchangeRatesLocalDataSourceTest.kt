package com.sharmadhiraj.mycurrencyexchange.data.local


import com.sharmadhiraj.mycurrencyexchange.data.local.dao.ExchangeRatesDao
import com.sharmadhiraj.mycurrencyexchange.data.local.entity.ExchangeRatesEntity
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.unmockkAll
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Test

class ExchangeRatesLocalDataSourceTest {

    private val exchangeRatesDao: ExchangeRatesDao = mockk()
    private val localDataSource = ExchangeRatesLocalDataSource(exchangeRatesDao)

    @Test
    fun `saveExchangeRates should insert data into the database`() = runBlocking {
        // Given
        val exchangeRatesEntity = createExchangeRatesEntity()

        // Stubbing the behavior of the DAO
        coEvery { exchangeRatesDao.saveExchangeRates(any()) } returns Unit
        coEvery { exchangeRatesDao.getExchangeRates() } returns exchangeRatesEntity

        // When
        localDataSource.saveExchangeRates(exchangeRatesEntity)

        // Then
        val storedEntity = exchangeRatesDao.getExchangeRates()
        assertNotNull(storedEntity)
        assertEquals(exchangeRatesEntity.base, storedEntity!!.base)
        assertEquals(exchangeRatesEntity.timestamp, storedEntity.timestamp)
        assertEquals(exchangeRatesEntity.rates, storedEntity.rates)
    }

    @Test
    fun `getExchangeRates should return data from the database`() = runBlocking {
        // Given
        val exchangeRatesEntity = createExchangeRatesEntity()

        // Stubbing the behavior of the DAO
        coEvery { exchangeRatesDao.getExchangeRates() } returns exchangeRatesEntity

        // When
        val retrievedEntity = localDataSource.getExchangeRates()

        // Then
        assertNotNull(retrievedEntity)
        assertEquals(exchangeRatesEntity.base, retrievedEntity!!.base)
        assertEquals(exchangeRatesEntity.timestamp, retrievedEntity.timestamp)
        assertEquals(exchangeRatesEntity.rates, retrievedEntity.rates)
    }

    @After
    fun tearDown() {
        clearAllMocks()
        unmockkAll()
    }

    private fun createExchangeRatesEntity(): ExchangeRatesEntity {
        return ExchangeRatesEntity("USD", 1705024800, mapOf("EUR" to 1.5, "GBP" to 1.2))
    }
}
