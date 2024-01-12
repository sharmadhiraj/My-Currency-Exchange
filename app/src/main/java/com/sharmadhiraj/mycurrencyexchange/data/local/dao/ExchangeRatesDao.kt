package com.sharmadhiraj.mycurrencyexchange.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sharmadhiraj.mycurrencyexchange.data.local.entity.ExchangeRatesEntity

@Dao
interface ExchangeRatesDao {

    @Query("SELECT * FROM exchange_rates LIMIT 1")
    suspend fun getExchangeRates(): ExchangeRatesEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveExchangeRates(entity: ExchangeRatesEntity)
}