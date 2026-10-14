package com.sharmadhiraj.mycurrencyexchange.data.remote

import com.sharmadhiraj.mycurrencyexchange.data.remote.api.ApiException
import com.sharmadhiraj.mycurrencyexchange.data.remote.api.ApiService
import com.sharmadhiraj.mycurrencyexchange.data.remote.model.ExchangeRatesApiResponse
import com.sharmadhiraj.mycurrencyexchange.domain.model.Currency
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test
import retrofit2.Response

class CurrenciesRemoteDataSourceTest {

    private val apiService: ApiService = mockk()
    private val remoteDataSource = CurrenciesRemoteDataSource(apiService, APP_ID)

    private fun ratesResponse(rates: Map<String, Double>?) =
        Response.success(ExchangeRatesApiResponse(null, null, null, null, rates))

    @Test
    fun `joins names with rates and sorts by code`() = runBlocking {
        coEvery { apiService.getLatestExchangeRates(APP_ID) } returns
            ratesResponse(mapOf("GBP" to 1.2, "EUR" to 1.5))
        coEvery { apiService.getCurrencies(APP_ID) } returns
            Response.success(mapOf("GBP" to "Pound", "EUR" to "Euro"))

        val result = remoteDataSource.getCurrencies()

        assertEquals(listOf(Currency("EUR", "Euro", 1.5), Currency("GBP", "Pound", 1.2)), result)
    }

    @Test
    fun `drops currencies that have no rate`() = runBlocking {
        coEvery { apiService.getLatestExchangeRates(APP_ID) } returns ratesResponse(mapOf("EUR" to 1.5))
        coEvery { apiService.getCurrencies(APP_ID) } returns
            Response.success(mapOf("EUR" to "Euro", "XXX" to "Unknown"))

        assertEquals(listOf(Currency("EUR", "Euro", 1.5)), remoteDataSource.getCurrencies())
    }

    @Test
    fun `throws ApiException on unsuccessful status`() {
        coEvery { apiService.getLatestExchangeRates(APP_ID) } returns
            Response.error(404, "".toResponseBody())
        coEvery { apiService.getCurrencies(APP_ID) } returns Response.success(mapOf("EUR" to "Euro"))

        val exception = assertThrows(ApiException::class.java) {
            runBlocking { remoteDataSource.getCurrencies() }
        }

        assertEquals("Unsuccessful response status code 404", exception.message)
    }

    @Test
    fun `throws ApiException on empty body`() {
        coEvery { apiService.getLatestExchangeRates(APP_ID) } returns Response.success(null)
        coEvery { apiService.getCurrencies(APP_ID) } returns Response.success(mapOf("EUR" to "Euro"))

        val exception = assertThrows(ApiException::class.java) {
            runBlocking { remoteDataSource.getCurrencies() }
        }

        assertEquals("Response body is empty", exception.message)
    }

    @Test
    fun `throws ApiException on network failure`() {
        coEvery { apiService.getLatestExchangeRates(APP_ID) } throws RuntimeException("Network error")
        coEvery { apiService.getCurrencies(APP_ID) } returns Response.success(mapOf("EUR" to "Euro"))

        val exception = assertThrows(ApiException::class.java) {
            runBlocking { remoteDataSource.getCurrencies() }
        }

        assertEquals("Request failed: Network error", exception.message)
    }

    @Test
    fun `throws ApiException when no rates are returned`() {
        coEvery { apiService.getLatestExchangeRates(APP_ID) } returns ratesResponse(null)
        coEvery { apiService.getCurrencies(APP_ID) } returns Response.success(mapOf("EUR" to "Euro"))

        assertThrows(ApiException::class.java) {
            runBlocking { remoteDataSource.getCurrencies() }
        }
    }

    private companion object {
        const val APP_ID = "test-app-id"
    }
}
