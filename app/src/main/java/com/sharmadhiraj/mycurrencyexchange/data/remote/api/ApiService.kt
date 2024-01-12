package com.sharmadhiraj.mycurrencyexchange.data.remote.api

import com.sharmadhiraj.mycurrencyexchange.data.model.ExchangeRatesApiResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("latest.json")
    suspend fun getLatestExchangeRates(@Query("app_id") appId: String): Response<ExchangeRatesApiResponse>
}

