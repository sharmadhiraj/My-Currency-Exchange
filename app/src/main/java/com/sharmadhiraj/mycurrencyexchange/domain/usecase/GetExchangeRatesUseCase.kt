package com.sharmadhiraj.mycurrencyexchange.domain.usecase

import com.sharmadhiraj.mycurrencyexchange.domain.model.ExchangeRates
import com.sharmadhiraj.mycurrencyexchange.domain.repository.ExchangeRatesRepository
import javax.inject.Inject

class GetExchangeRatesUseCase @Inject constructor(
    private val repository: ExchangeRatesRepository,
) {
    suspend operator fun invoke(forceRefresh: Boolean = false): ExchangeRates {
        return repository.getExchangeRates(forceRefresh)
    }
}
