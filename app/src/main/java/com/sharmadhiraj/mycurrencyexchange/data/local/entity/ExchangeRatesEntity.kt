package com.sharmadhiraj.mycurrencyexchange.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.util.Date

@Entity(tableName = "exchange_rates")
data class ExchangeRatesEntity(
    @PrimaryKey
    val base: String,
    val timestamp: Long,
    val rates: Map<String, Double>,
    @ColumnInfo(name = "updated_at")
    @SerializedName(value = "updated_at")
    val updatedAt: Date = Date(System.currentTimeMillis())
)
