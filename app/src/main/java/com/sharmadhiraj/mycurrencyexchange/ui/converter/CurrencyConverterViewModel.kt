package com.sharmadhiraj.mycurrencyexchange.ui.converter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sharmadhiraj.mycurrencyexchange.R
import com.sharmadhiraj.mycurrencyexchange.domain.exception.ExchangeRatesUnavailableException
import com.sharmadhiraj.mycurrencyexchange.domain.model.ExchangeRates
import com.sharmadhiraj.mycurrencyexchange.domain.usecase.ConvertCurrencyUseCase
import com.sharmadhiraj.mycurrencyexchange.domain.usecase.GetExchangeRatesUseCase
import com.sharmadhiraj.mycurrencyexchange.ui.util.parseAmount
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CurrencyConverterViewModel @Inject constructor(
    private val getExchangeRates: GetExchangeRatesUseCase,
    private val convertCurrency: ConvertCurrencyUseCase,
) : ViewModel() {

    private val _uiState = MutableLiveData<ConverterUiState>(ConverterUiState.Loading)
    val uiState: LiveData<ConverterUiState> get() = _uiState

    private var rates: ExchangeRates? = null
    private var amountInput = DEFAULT_AMOUNT
    private var selectedCode: String? = null
    private var isRefreshing = false
    private var loadJob: Job? = null

    init {
        load(showLoading = true, forceRefresh = false)
    }

    fun retry() = load(showLoading = true, forceRefresh = true)

    fun refresh() = load(showLoading = false, forceRefresh = true)

    fun onAmountChanged(input: String) {
        amountInput = input
        publishContent()
    }

    fun onCurrencySelected(code: String) {
        selectedCode = code
        publishContent()
    }

    private fun load(showLoading: Boolean, forceRefresh: Boolean) {
        loadJob?.cancel()
        if (showLoading) {
            _uiState.value = ConverterUiState.Loading
        } else {
            isRefreshing = true
            publishContent()
        }
        loadJob = viewModelScope.launch {
            try {
                val result = getExchangeRates(forceRefresh)
                rates = result
                selectedCode = resolveSelectedCode(result)
                isRefreshing = false
                publishContent()
            } catch (e: ExchangeRatesUnavailableException) {
                isRefreshing = false
                val current = rates
                if (current == null) {
                    _uiState.value = ConverterUiState.Error(
                        if (e.isNetworkError) R.string.error_no_connection else R.string.error_loading_rates
                    )
                } else {
                    rates = current.copy(isStale = true)
                    publishContent()
                }
            }
        }
    }

    private fun resolveSelectedCode(result: ExchangeRates): String {
        val codes = result.currencies.map { it.code }
        return selectedCode?.takeIf { it in codes }
            ?: DEFAULT_CODE.takeIf { it in codes }
            ?: codes.first()
    }

    private fun publishContent() {
        val rates = rates ?: return
        val code = selectedCode ?: return
        val amount = parseAmount(amountInput)
        _uiState.value = ConverterUiState.Content(
            currencies = rates.currencies,
            selectedCode = code,
            amountInput = amountInput,
            isAmountValid = amount != null,
            results = amount?.let { convertCurrency(it, code, rates.currencies) }.orEmpty(),
            lastUpdatedMillis = rates.lastUpdatedMillis,
            isStale = rates.isStale,
            isRefreshing = isRefreshing,
        )
    }

    private companion object {
        const val DEFAULT_AMOUNT = "1"
        const val DEFAULT_CODE = "USD"
    }
}
