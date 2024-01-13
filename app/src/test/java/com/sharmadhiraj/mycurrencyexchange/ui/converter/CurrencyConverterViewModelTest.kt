package com.sharmadhiraj.mycurrencyexchange.ui.converter

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.sharmadhiraj.mycurrencyexchange.data.repository.ExchangeRatesRepositoryImpl
import com.sharmadhiraj.mycurrencyexchange.domain.exception.ExchangeRatesFetchException
import com.sharmadhiraj.mycurrencyexchange.domain.model.ExchangeRates
import com.sharmadhiraj.mycurrencyexchange.helpers.MainDispatcherRule
import com.sharmadhiraj.mycurrencyexchange.ui.converter.ConverterViewState.Success
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.unmockkAll
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class CurrencyConverterViewModelTest {

    private lateinit var viewModel: CurrencyConverterViewModel
    private lateinit var repository: ExchangeRatesRepositoryImpl
    private val testDispatcher = StandardTestDispatcher()
    private val testCoroutineScope = TestScope(testDispatcher)

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
        //Given
        coEvery { repository.getExchangeRates() } returns mockExchangeRates

        //When
        viewModel.fetchExchangeRates()

        //Then
        assert(viewModel.viewState.value is ConverterViewState.Loading)
    }

    @Test
    fun `fetchExchangeRates success should update live data`() {
        //Given
        coEvery { repository.getExchangeRates() } returns mockExchangeRates

        //When
        viewModel.fetchExchangeRates()

        //Then
        testCoroutineScope.advanceUntilIdle()
        coVerify {
            repository.getExchangeRates()
        }
        assert(viewModel.viewState.value is Success)
        assertEquals(mockExchangeRates, (viewModel.viewState.value as Success).exchangeRates)
    }

    @Test
    fun `fetchExchangeRates failure should update error live data`() {
        //Given
        coEvery { repository.getExchangeRates() } throws ExchangeRatesFetchException("Error")

        //When
        viewModel.fetchExchangeRates()
        coVerify {
            repository.getExchangeRates()
        }

        //Then
        testCoroutineScope.advanceUntilIdle()
        assert(viewModel.viewState.value is ConverterViewState.Error)
        assertEquals("Error", (viewModel.viewState.value as ConverterViewState.Error).errorMessage)
    }


    @After
    fun tearDown() {
        clearAllMocks()
        unmockkAll()
    }

    companion object {
        private val mockExchangeRates =
            ExchangeRates(
                "USD",
                1705024800, mapOf(
                    "EUR" to 1.5, "GBP" to 1.2
                )
            )
    }
}
