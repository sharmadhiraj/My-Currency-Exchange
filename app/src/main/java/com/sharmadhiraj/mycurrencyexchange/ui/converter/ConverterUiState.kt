package com.sharmadhiraj.mycurrencyexchange.ui.converter

import androidx.annotation.StringRes
import com.sharmadhiraj.mycurrencyexchange.domain.model.ConvertedAmount
import com.sharmadhiraj.mycurrencyexchange.domain.model.Currency

sealed interface ConverterUiState {

    data object Loading : ConverterUiState

    data class Error(@param:StringRes val messageRes: Int) : ConverterUiState

    data class Content(
        val currencies: List<Currency>,
        val selectedCode: String,
        val amountInput: String,
        val isAmountValid: Boolean,
        val results: List<ConvertedAmount>,
        val lastUpdatedMillis: Long?,
        val isStale: Boolean,
        val isRefreshing: Boolean,
    ) : ConverterUiState
}
