package com.sharmadhiraj.mycurrencyexchange.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sharmadhiraj.mycurrencyexchange.data.local.entity.CurrencyEntity

@Dao
interface CurrencyDao {

    @Query("SELECT * FROM currency")
    suspend fun getCurrencies(): List<CurrencyEntity>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveCurrencies(currencies: List<CurrencyEntity>)
}