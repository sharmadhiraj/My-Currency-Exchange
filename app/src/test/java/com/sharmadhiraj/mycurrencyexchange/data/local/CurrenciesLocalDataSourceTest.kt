package com.sharmadhiraj.mycurrencyexchange.data.local

import com.sharmadhiraj.mycurrencyexchange.data.local.dao.CurrencyDao
import com.sharmadhiraj.mycurrencyexchange.data.local.entity.CurrencyEntity
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class CurrenciesLocalDataSourceTest {

    private val currencyDao: CurrencyDao = mockk()
    private val localDataSource = CurrenciesLocalDataSource(currencyDao)
    private val entities = listOf(
        CurrencyEntity("USD", "United States Dollar", 1.0),
        CurrencyEntity("EUR", "Euro", 1.5),
    )

    @Test
    fun `getCurrencies returns what the dao holds`() = runBlocking {
        coEvery { currencyDao.getCurrencies() } returns entities

        assertEquals(entities, localDataSource.getCurrencies())
    }

    @Test
    fun `replaceCurrencies delegates to the dao`() = runBlocking {
        coEvery { currencyDao.replaceCurrencies(any()) } returns Unit

        localDataSource.replaceCurrencies(entities)

        coVerify { currencyDao.replaceCurrencies(entities) }
    }
}
