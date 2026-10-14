package com.sharmadhiraj.mycurrencyexchange.di

import com.sharmadhiraj.mycurrencyexchange.data.repository.CurrenciesRepositoryImpl
import com.sharmadhiraj.mycurrencyexchange.domain.repository.ExchangeRatesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindExchangeRatesRepository(impl: CurrenciesRepositoryImpl): ExchangeRatesRepository
}
