package com.sharmadhiraj.mycurrencyexchange.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sharmadhiraj.mycurrencyexchange.data.local.dao.ExchangeRatesDao
import com.sharmadhiraj.mycurrencyexchange.data.local.entity.ExchangeRatesEntity

@Database(entities = [ExchangeRatesEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun exchangeRatesDao(): ExchangeRatesDao
}