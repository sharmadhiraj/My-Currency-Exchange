package com.sharmadhiraj.mycurrencyexchange.data.remote.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {

    private const val BASE_URL = "https://openexchangerates.org/api/"

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val apiService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }

    suspend fun <T> fetchData(apiCall: suspend () -> T): T {
        try {
            val response = apiCall.invoke()
            if (response is Map<*, *> || response is List<*>) {
                return response
            } else {
                throw ApiException("Invalid response type")
            }
        } catch (e: Exception) {
            throw ApiException("API error: ${e.message}", e)
        }
    }

}