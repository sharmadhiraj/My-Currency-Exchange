package com.sharmadhiraj.mycurrencyexchange.data.repository

import com.sharmadhiraj.mycurrencyexchange.data.local.CurrenciesLocalDataSource
import com.sharmadhiraj.mycurrencyexchange.data.local.entity.CurrencyEntity
import com.sharmadhiraj.mycurrencyexchange.data.remote.CurrenciesRemoteDataSource
import com.sharmadhiraj.mycurrencyexchange.data.remote.api.ApiException
import com.sharmadhiraj.mycurrencyexchange.domain.exception.ExchangeRatesUnavailableException
import com.sharmadhiraj.mycurrencyexchange.domain.model.Currency
import com.sharmadhiraj.mycurrencyexchange.domain.model.ExchangeRates
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.IOException

class CurrenciesRepositoryImplTest {

    private val remote: CurrenciesRemoteDataSource = mockk()
    private val local: CurrenciesLocalDataSource = mockk()
    private val cachePolicy: RatesCachePolicy = mockk()
    private val repository = CurrenciesRepositoryImpl(remote, local, cachePolicy)

    private val cachedEntities = listOf(CurrencyEntity("USD", "United States Dollar", 1.0))
    private val cached = listOf(Currency("USD", "United States Dollar", 1.0))
    private val fetched = listOf(Currency("USD", "United States Dollar", 1.5))

    private fun givenCache(entities: List<CurrencyEntity>, fresh: Boolean) {
        coEvery { local.getCurrencies() } returns entities
        every { cachePolicy.isFresh() } returns fresh
        every { cachePolicy.lastRefreshMillis() } returns LAST_REFRESH
    }

    @Test
    fun `fresh cache is returned without calling the network`() = runBlocking {
        givenCache(cachedEntities, fresh = true)

        val result = repository.getExchangeRates()

        assertEquals(ExchangeRates(cached, LAST_REFRESH), result)
        coVerify(exactly = 0) { remote.getCurrencies() }
    }

    @Test
    fun `expired cache triggers a fetch that is saved and returned`() = runBlocking {
        givenCache(cachedEntities, fresh = false)
        coEvery { remote.getCurrencies() } returns fetched
        coEvery { local.replaceCurrencies(any()) } returns Unit
        every { cachePolicy.markRefreshed() } returns Unit

        val result = repository.getExchangeRates()

        assertEquals(ExchangeRates(fetched, LAST_REFRESH), result)
        coVerify { local.replaceCurrencies(listOf(CurrencyEntity("USD", "United States Dollar", 1.5))) }
        verify { cachePolicy.markRefreshed() }
    }

    @Test
    fun `forceRefresh ignores a fresh cache`() = runBlocking {
        givenCache(cachedEntities, fresh = true)
        coEvery { remote.getCurrencies() } returns fetched
        coEvery { local.replaceCurrencies(any()) } returns Unit
        every { cachePolicy.markRefreshed() } returns Unit

        val result = repository.getExchangeRates(forceRefresh = true)

        assertEquals(fetched, result.currencies)
        coVerify { remote.getCurrencies() }
    }

    @Test
    fun `network failure falls back to stale cache`() = runBlocking {
        givenCache(cachedEntities, fresh = false)
        coEvery { remote.getCurrencies() } throws ApiException("Error")

        val result = repository.getExchangeRates()

        assertEquals(ExchangeRates(cached, LAST_REFRESH, isStale = true), result)
        verify(exactly = 0) { cachePolicy.markRefreshed() }
    }

    @Test
    fun `network failure with empty cache throws and flags connection problems`() {
        givenCache(emptyList(), fresh = false)
        coEvery { remote.getCurrencies() } throws ApiException("Error", IOException("offline"))

        val exception = assertThrows(ExchangeRatesUnavailableException::class.java) {
            runBlocking { repository.getExchangeRates() }
        }

        assertTrue(exception.isNetworkError)
    }

    @Test
    fun `service failure with empty cache is not flagged as a connection problem`() {
        givenCache(emptyList(), fresh = false)
        coEvery { remote.getCurrencies() } throws ApiException("Unsuccessful response status code 401")

        val exception = assertThrows(ExchangeRatesUnavailableException::class.java) {
            runBlocking { repository.getExchangeRates() }
        }

        assertFalse(exception.isNetworkError)
    }

    private companion object {
        const val LAST_REFRESH = 1_000L
    }
}
