package com.sharmadhiraj.mycurrencyexchange.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sharmadhiraj.mycurrencyexchange.data.local.dao.CurrencyDao
import com.sharmadhiraj.mycurrencyexchange.data.local.entity.CurrencyEntity
import javax.inject.Singleton

@Database(
    entities = [CurrencyEntity::class],
    version = 1,
    exportSchema = false
)
@Singleton
abstract class AppDatabase : RoomDatabase() {
    abstract fun currencyDao(): CurrencyDao
}