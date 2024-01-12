package com.sharmadhiraj.mycurrencyexchange.ui.converter

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.sharmadhiraj.mycurrencyexchange.data.repository.ExchangeRatesRepositoryImpl
import com.sharmadhiraj.mycurrencyexchange.domain.exception.ExchangeRatesFetchException
import com.sharmadhiraj.mycurrencyexchange.domain.model.ExchangeRates
import com.sharmadhiraj.mycurrencyexchange.helpers.MainDispatcherRule
import com.sharmadhiraj.mycurrencyexchange.ui.converter.ConverterViewState.Success
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class CurrencyConverterViewModelTest {

    private lateinit var viewModel: CurrencyConverterViewModel
    private lateinit var repository: ExchangeRatesRepositoryImpl

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setup() {
        repository = mockk()
        viewModel = CurrencyConverterViewModel(repository)
    }

    @Test
    fun `fetchExchangeRates success should update live loading data`() {
        coEvery { repository.getExchangeRates() } returns mockExchangeRates
        viewModel.fetchExchangeRates()
        assert(viewModel.viewState.value is ConverterViewState.Loading)
    }

    @Test
    fun `fetchExchangeRates success should update live data`() {
        coEvery { repository.getExchangeRates() } returns mockExchangeRates
        viewModel.fetchExchangeRates()
        coVerify {
            repository.getExchangeRates()
        }
        assert(viewModel.viewState.value is Success)
        assertEquals(mockExchangeRates, (viewModel.viewState.value as Success).exchangeRates)
    }

    @Test
    fun `fetchExchangeRates failure should update error live data`() {
        coEvery { repository.getExchangeRates() } throws ExchangeRatesFetchException("Error")
        viewModel.fetchExchangeRates()
        coVerify {
            repository.getExchangeRates()
        }
        assert(viewModel.viewState.value is ConverterViewState.Error)
        assertEquals("Error", (viewModel.viewState.value as ConverterViewState.Error).errorMessage)
    }

    @Test
    fun `convertCurrency should update convertedAmounts live data`() {
        coEvery { repository.getExchangeRates() } returns mockExchangeRates
        viewModel.fetchExchangeRates()
        viewModel.convertCurrency(1.0, "AED")
        val expectedConvertedAmounts = mapOf("USD" to 15.0, "GBP" to 12.0)
        assertEquals(expectedConvertedAmounts, viewModel.convertedAmounts.value)
    }

    companion object {
        private val mockExchangeRates =
            ExchangeRates("USD", 1705024800, mapOf("EUR" to 1.5, "GBP" to 1.2))
    }
}
