package com.sharmadhiraj.mycurrencyexchange.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.sharmadhiraj.mycurrencyexchange.data.local.AppDatabase
import com.sharmadhiraj.mycurrencyexchange.data.local.dao.CurrencyDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object StorageModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, "my_currency_exchange_database")
            .build()
    }

    @Provides
    fun provideCurrencyDao(database: AppDatabase): CurrencyDao = database.currencyDao()

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences("exchange_data_preferences", Context.MODE_PRIVATE)
    }
}
