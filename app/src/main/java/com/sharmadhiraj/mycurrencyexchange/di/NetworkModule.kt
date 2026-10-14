package com.sharmadhiraj.mycurrencyexchange.di

import com.sharmadhiraj.mycurrencyexchange.BuildConfig
import com.sharmadhiraj.mycurrencyexchange.data.remote.api.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val BASE_URL = "https://openexchangerates.org/api/"

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService = retrofit.create(ApiService::class.java)

    @Provides
    @OpenExchangeRatesAppId
    fun provideAppId(): String = BuildConfig.OPEN_EXCHANGE_RATES_APP_ID
}
