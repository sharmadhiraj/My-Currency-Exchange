package com.sharmadhiraj.mycurrencyexchange.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.sharmadhiraj.mycurrencyexchange.data.local.entity.CurrencyEntity

@Dao
abstract class CurrencyDao {

    @Query("SELECT * FROM currency ORDER BY code")
    abstract suspend fun getCurrencies(): List<CurrencyEntity>

    @Transaction
    open suspend fun replaceCurrencies(currencies: List<CurrencyEntity>) {
        deleteAll()
        insertAll(currencies)
    }

    @Query("DELETE FROM currency")
    protected abstract suspend fun deleteAll()

    @Insert
    protected abstract suspend fun insertAll(currencies: List<CurrencyEntity>)
}
