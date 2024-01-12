package com.sharmadhiraj.mycurrencyexchange.ui.converter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sharmadhiraj.mycurrencyexchange.data.repository.ExchangeRatesRepositoryImpl
import com.sharmadhiraj.mycurrencyexchange.domain.exception.ExchangeRatesFetchException
import com.sharmadhiraj.mycurrencyexchange.domain.model.ExchangeRates
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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
                _viewState.postValue(ConverterViewState.Success(rates))
            } catch (e: ExchangeRatesFetchException) {
                _viewState.postValue(ConverterViewState.Error(e.message ?: "Unknown error"))
            }
        }
    }

    fun convertCurrency(amount: Double, selectedCurrency: String) {
        val exchangeRates = (_viewState.value as? ConverterViewState.Success)?.exchangeRates?.rates
        if (exchangeRates != null) {
            val convertedAmounts = exchangeRates.mapValues { (_, rate) ->
                amount * rate / exchangeRates[selectedCurrency]!!
            }
            _convertedAmounts.value = convertedAmounts
        }
    }
}

sealed class ConverterViewState {
    data object Loading : ConverterViewState()
    data class Success(val exchangeRates: ExchangeRates) : ConverterViewState()
    data class Error(val errorMessage: String) : ConverterViewState()
}
