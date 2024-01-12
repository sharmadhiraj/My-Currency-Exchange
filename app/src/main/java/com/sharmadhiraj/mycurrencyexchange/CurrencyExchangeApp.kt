package com.sharmadhiraj.mycurrencyexchange

import android.app.Application
import androidx.room.Room
import com.sharmadhiraj.mycurrencyexchange.data.local.AppDatabase

class CurrencyExchangeApp : Application() {

    companion object {
        lateinit var database: AppDatabase
    }

    override fun onCreate() {
        super.onCreate()

        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "my_currency_exchange_database"
        ).build()

    }
}