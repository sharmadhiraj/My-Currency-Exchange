package com.sharmadhiraj.mycurrencyexchange.data.remote

import com.sharmadhiraj.mycurrencyexchange.data.remote.api.ApiException
import com.sharmadhiraj.mycurrencyexchange.data.remote.api.ApiService
import com.sharmadhiraj.mycurrencyexchange.data.remote.model.ExchangeRatesApiResponse
import com.sharmadhiraj.mycurrencyexchange.domain.model.Currency
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.unmockkAll
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody
import org.junit.After
import org.junit.Test
import retrofit2.Response

class CurrenciesRemoteDataSourceTest {

    private val apiService: ApiService = mockk()
    private val remoteDataSource = CurrenciesRemoteDataSource(apiService)

    @Test
    fun `getExchangeRates returns ExchangeRates on successful API response`() = runBlocking {
        // Given
        val mockExchangeRates = ExchangeRatesApiResponse(
            null,
            null,
            1705024800,
            "USD",
            mapOf("EUR" to 1.5, "GBP" to 1.2)
        )
        coEvery { apiService.getLatestExchangeRates(any()) } returns Response.success(
            mockExchangeRates
        )

        // When
        val result = remoteDataSource.getExchangeRates()

        // Then
        assertEquals(
            Currency(
                mockExchangeRates.base!!,
                mockExchangeRates.timestamp!!,
                mockExchangeRates.rates!!
            ), result
        )
    }

    @Test
    fun `getExchangeRates throws ApiException on API error`() = runBlocking {
        // Given
        val errorResponseBody: ResponseBody = mockk()
        coEvery { errorResponseBody.contentType() } returns null
        coEvery { errorResponseBody.contentLength() } returns 0
        coEvery { apiService.getLatestExchangeRates(any()) } returns Response.error(
            404,
            errorResponseBody
        )

        // When
        val exception = try {
            remoteDataSource.getExchangeRates()
            null
        } catch (exception: RuntimeException) {
            exception
        }

        // Then
        assert(exception is ApiException)
        assertEquals("API error: Unsuccessful response status code 404", exception?.message)
    }

    @Test
    fun `getExchangeRates throws ApiException on null or empty response data`() = runBlocking {
        // Given
        coEvery { apiService.getLatestExchangeRates(any()) } returns Response.success(null)
//        coEvery { apiService.getLatestExchangeRates(any()) } returns Response.success(
//            ExchangeRatesApiResponse(null, null, null, null, null)
//        )
//        coEvery { apiService.getLatestExchangeRates(any()) } returns Response.success(
//            ExchangeRatesApiResponse(null, null, base = "USD", timestamp = 1234567890, rates = null)
//        )

        // When
        val exception = try {
            remoteDataSource.getExchangeRates()
            null
        } catch (exception: RuntimeException) {
            exception
        }

        // Then
        assert(exception is ApiException)
        assertEquals("API error: Response null or empty", exception?.message)
    }

    @Test
    fun `getExchangeRates throws ApiException on network error`() = runBlocking {
        // Given
        coEvery { apiService.getLatestExchangeRates(any()) } throws RuntimeException("Network error")

        // When
        val exception = try {
            remoteDataSource.getExchangeRates()
            null
        } catch (exception: RuntimeException) {
            exception
        }

        // Then
        assert(exception is ApiException)
        assertEquals("API error: Network error", exception?.message)
    }

    @After
    fun tearDown() {
        clearAllMocks()
        unmockkAll()
    }
}
