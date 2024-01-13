package com.sharmadhiraj.mycurrencyexchange.ui.converter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sharmadhiraj.mycurrencyexchange.data.repository.CurrenciesRepositoryImpl
import com.sharmadhiraj.mycurrencyexchange.domain.exception.ExchangeRatesFetchException
import com.sharmadhiraj.mycurrencyexchange.domain.model.Currency
import com.sharmadhiraj.mycurrencyexchange.util.CommonUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class CurrencyConverterViewModel @Inject constructor(private val repository: CurrenciesRepositoryImpl) :
    ViewModel() {

    private val _viewState = MutableLiveData<ConverterViewState>()
    val viewState: LiveData<ConverterViewState> get() = _viewState

    private val _convertedAmounts = MutableLiveData<List<Currency>>()
    val convertedAmounts: LiveData<List<Currency>> get() = _convertedAmounts

    fun fetchExchangeRates() {
        _viewState.value = ConverterViewState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val rates = repository.getCurrencies()
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
        val exchangeRates = (_viewState.value as? ConverterViewState.Success)?.currencies
        if (exchangeRates != null) {
            _convertedAmounts.value =
                CommonUtil.convertCurrencies(amount, selectedCurrency, exchangeRates)
        }
    }
}

sealed class ConverterViewState {
    data object Loading : ConverterViewState()
    data class Success(val currencies: List<Currency>) : ConverterViewState()
    data class Error(val errorMessage: String) : ConverterViewState()
}
