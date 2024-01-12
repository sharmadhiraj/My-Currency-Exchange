package com.sharmadhiraj.mycurrencyexchange.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.sharmadhiraj.mycurrencyexchange.data.local.dao.ExchangeRatesDao
import com.sharmadhiraj.mycurrencyexchange.data.local.entity.ExchangeRatesEntity
import javax.inject.Singleton

@Database(entities = [ExchangeRatesEntity::class], version = 1, exportSchema = false)
@Singleton
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun exchangeRatesDao(): ExchangeRatesDao
}