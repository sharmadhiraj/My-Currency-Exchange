package com.sharmadhiraj.mycurrencyexchange.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exchange_rates")
data class ExchangeRatesEntity(
    @PrimaryKey
    val base: String,
    val timestamp: Long,
    val rates: Map<String, Double>
)
