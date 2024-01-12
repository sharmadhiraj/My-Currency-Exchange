package com.sharmadhiraj.mycurrencyexchange.ui.converter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sharmadhiraj.mycurrencyexchange.data.repository.ExchangeRatesRepositoryImpl
import com.sharmadhiraj.mycurrencyexchange.domain.exception.ExchangeRatesFetchException
import com.sharmadhiraj.mycurrencyexchange.domain.model.ExchangeRates
import com.sharmadhiraj.mycurrencyexchange.util.CommonUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class CurrencyConverterViewModel @Inject constructor(private val repository: ExchangeRatesRepositoryImpl) :
    ViewModel() {

    private val _viewState = MutableLiveData<ConverterViewState>()
    val viewState: LiveData<ConverterViewState> get() = _viewState

    private val _convertedAmounts = MutableLiveData<Map<String, Double>>()
    val convertedAmounts: LiveData<Map<String, Double>> get() = _convertedAmounts

    fun fetchExchangeRates() {
        _viewState.value = ConverterViewState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val rates = repository.getExchangeRates()
                withContext(Dispatchers.Main) {
                    _viewState.value = ConverterViewState.Success(rates)
                }
            } catch (e: ExchangeRatesFetchException) {
                withContext(Dispatchers.Main) {
                    _viewState.value = ConverterViewState.Error(e.message ?: "Unknown error")
                }
            }
        }
    }

    fun convertCurrency(amount: Double, selectedCurrency: String) {
        val exchangeRates = (_viewState.value as? ConverterViewState.Success)?.exchangeRates?.rates
        if (exchangeRates != null) {
            _convertedAmounts.value =
                CommonUtil.convertCurrency(amount, selectedCurrency, exchangeRates)
        }
    }
}

sealed class ConverterViewState {
    data object Loading : ConverterViewState()
    data class Success(val exchangeRates: ExchangeRates) : ConverterViewState()
    data class Error(val errorMessage: String) : ConverterViewState()
}
