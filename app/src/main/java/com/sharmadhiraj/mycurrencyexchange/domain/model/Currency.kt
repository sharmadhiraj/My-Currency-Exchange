package com.sharmadhiraj.mycurrencyexchange.domain.model

data class Currency(
    val code: String,
    val name: String,
    val rate: Double,
) {
    fun fullName(): String {
        return "($code) $name"
    }
}