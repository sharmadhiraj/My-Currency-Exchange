package com.sharmadhiraj.mycurrencyexchange.di


import android.content.Context
import androidx.room.Room
import com.sharmadhiraj.mycurrencyexchange.data.local.AppDatabase
import com.sharmadhiraj.mycurrencyexchange.data.local.dao.CurrencyDao
import com.sharmadhiraj.mycurrencyexchange.data.remote.api.ApiClient
import com.sharmadhiraj.mycurrencyexchange.data.remote.api.ApiService
import com.sharmadhiraj.mycurrencyexchange.data.repository.ExchangeDataCacheValidator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideApiService(): ApiService {
        return ApiClient.apiService
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "my_currency_exchange_database"
        ).build()
    }


    @Provides
    fun provideExchangeRatesDao(database: AppDatabase): CurrencyDao {
        return database.currencyDao()
    }

    @Provides
    @Singleton
    fun provideExchangeDataCacheValidator(@ApplicationContext context: Context): ExchangeDataCacheValidator {
        val sharedPreferences =
            context.getSharedPreferences("exchange_data_preferences", Context.MODE_PRIVATE)
        return ExchangeDataCacheValidator(sharedPreferences)
    }

}
