package com.sharmadhiraj.mycurrencyexchange.data.mapper

import com.sharmadhiraj.mycurrencyexchange.data.local.entity.CurrencyEntity
import com.sharmadhiraj.mycurrencyexchange.domain.model.Currency

fun CurrencyEntity.toDomain() = Currency(code = code, name = name, rate = rate)

fun Currency.toEntity() = CurrencyEntity(code = code, name = name, rate = rate)

fun List<CurrencyEntity>.toDomainModels() = map { it.toDomain() }

fun List<Currency>.toEntities() = map { it.toEntity() }
