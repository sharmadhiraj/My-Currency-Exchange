package com.sharmadhiraj.mycurrencyexchange.ui.converter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sharmadhiraj.mycurrencyexchange.domain.exception.ExchangeRatesFetchException
import com.sharmadhiraj.mycurrencyexchange.domain.model.ExchangeRates
import com.sharmadhiraj.mycurrencyexchange.domain.repository.ExchangeRatesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CurrencyConverterViewModel(private val repository: ExchangeRatesRepository) : ViewModel() {

    private val _exchangeRates = MutableLiveData<ExchangeRates>()
    val exchangeRates: LiveData<ExchangeRates> get() = _exchangeRates

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    fun fetchExchangeRates() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val rates = repository.getExchangeRates()
                _exchangeRates.postValue(rates)
            } catch (e: ExchangeRatesFetchException) {
                _error.postValue(e.message)
            }
        }
    }

}
