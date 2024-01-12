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

    private val _exchangeRates = MutableLiveData<ExchangeRates>()
    val exchangeRates: LiveData<ExchangeRates> get() = _exchangeRates

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _convertedAmounts = MutableLiveData<Map<String, Double>>()
    val convertedAmounts: LiveData<Map<String, Double>> get() = _convertedAmounts


    fun fetchExchangeRates() {
        _isLoading.value = true
        _error.value = null
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val rates = repository.getExchangeRates()
                _exchangeRates.postValue(rates)
            } catch (e: ExchangeRatesFetchException) {
                _error.postValue(e.message)
            } finally {
                _isLoading.postValue(false)
            }
        }
    }

    fun convertCurrency(amount: Double, selectedCurrency: String) {
        val exchangeRates = _exchangeRates.value?.rates
        if (exchangeRates != null) {
            val convertedAmounts = exchangeRates.mapValues { (_, rate) ->
                amount * rate / exchangeRates[selectedCurrency]!!
            }
            _convertedAmounts.value = convertedAmounts
        }
    }


}
