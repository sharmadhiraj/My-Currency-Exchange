package com.sharmadhiraj.mycurrencyexchange.ui.converter

import com.sharmadhiraj.mycurrencyexchange.R
import com.sharmadhiraj.mycurrencyexchange.domain.exception.ExchangeRatesUnavailableException
import com.sharmadhiraj.mycurrencyexchange.domain.model.ConvertedAmount
import com.sharmadhiraj.mycurrencyexchange.domain.model.Currency
import com.sharmadhiraj.mycurrencyexchange.domain.model.ExchangeRates
import com.sharmadhiraj.mycurrencyexchange.domain.usecase.ConvertCurrencyUseCase
import com.sharmadhiraj.mycurrencyexchange.domain.usecase.GetExchangeRatesUseCase
import com.sharmadhiraj.mycurrencyexchange.helpers.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class CurrencyConverterViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val getExchangeRates: GetExchangeRatesUseCase = mockk()
    private val currencies = listOf(
        Currency("EUR", "Euro", 0.5),
        Currency("USD", "United States Dollar", 1.0),
    )
    private val rates = ExchangeRates(currencies, lastUpdatedMillis = 1_000L)

    private fun createViewModel() =
        CurrencyConverterViewModel(getExchangeRates, ConvertCurrencyUseCase())

    private fun CurrencyConverterViewModel.content() = uiState.value as ConverterUiState.Content

    @Test
    fun `loads rates on creation and defaults to USD with amount 1`() {
        coEvery { getExchangeRates(any()) } returns rates

        val content = createViewModel().content()

        assertEquals("USD", content.selectedCode)
        assertEquals("1", content.amountInput)
        assertEquals(
            listOf(
                ConvertedAmount("EUR", "Euro", 0.5),
                ConvertedAmount("USD", "United States Dollar", 1.0),
            ),
            content.results
        )
        assertEquals(1_000L, content.lastUpdatedMillis)
        assertFalse(content.isStale)
    }

    @Test
    fun `shows error when nothing can be loaded`() {
        coEvery { getExchangeRates(any()) } throws ExchangeRatesUnavailableException("boom")

        val state = createViewModel().uiState.value

        assertEquals(ConverterUiState.Error(R.string.error_loading_rates), state)
    }

    @Test
    fun `shows connection error when the failure is a network problem`() {
        coEvery { getExchangeRates(any()) } throws
            ExchangeRatesUnavailableException("offline", isNetworkError = true)

        val state = createViewModel().uiState.value

        assertEquals(ConverterUiState.Error(R.string.error_no_connection), state)
    }

    @Test
    fun `retry forces a refresh and recovers from error`() {
        coEvery { getExchangeRates(false) } throws ExchangeRatesUnavailableException("boom")
        coEvery { getExchangeRates(true) } returns rates
        val viewModel = createViewModel()

        viewModel.retry()

        assertTrue(viewModel.uiState.value is ConverterUiState.Content)
        coVerify { getExchangeRates(true) }
    }

    @Test
    fun `amount changes recompute results`() {
        coEvery { getExchangeRates(any()) } returns rates
        val viewModel = createViewModel()

        viewModel.onAmountChanged("10")

        assertEquals(listOf(5.0, 10.0), viewModel.content().results.map { it.amount })
    }

    @Test
    fun `invalid amount is flagged and clears results`() {
        coEvery { getExchangeRates(any()) } returns rates
        val viewModel = createViewModel()

        viewModel.onAmountChanged("")

        assertFalse(viewModel.content().isAmountValid)
        assertTrue(viewModel.content().results.isEmpty())
    }

    @Test
    fun `selecting a currency converts from that currency`() {
        coEvery { getExchangeRates(any()) } returns rates
        val viewModel = createViewModel()

        viewModel.onCurrencySelected("EUR")

        assertEquals("EUR", viewModel.content().selectedCode)
        assertEquals(listOf(1.0, 2.0), viewModel.content().results.map { it.amount })
    }

    @Test
    fun `refresh keeps selection and input`() {
        coEvery { getExchangeRates(any()) } returns rates
        val viewModel = createViewModel()
        viewModel.onCurrencySelected("EUR")
        viewModel.onAmountChanged("3")

        viewModel.refresh()

        val content = viewModel.content()
        assertEquals("EUR", content.selectedCode)
        assertEquals("3", content.amountInput)
        assertFalse(content.isRefreshing)
    }

    @Test
    fun `failed refresh keeps content and marks it stale`() {
        coEvery { getExchangeRates(false) } returns rates
        coEvery { getExchangeRates(true) } throws ExchangeRatesUnavailableException("boom")
        val viewModel = createViewModel()

        viewModel.refresh()

        val content = viewModel.content()
        assertTrue(content.isStale)
        assertFalse(content.isRefreshing)
        assertEquals(currencies, content.currencies)
    }
}
