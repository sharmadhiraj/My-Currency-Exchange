package com.sharmadhiraj.mycurrencyexchange.data.remote.api

import retrofit2.Response
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

    suspend fun <T> fetchData(apiCall: suspend () -> Response<T>?): T {
        try {
            val response: Response<T>? = apiCall.invoke()
            if (response != null) {
                if (response.isSuccessful) {
                    return response.body() ?: throw ApiException("Response body is empty")
                } else {
                    throw ApiException("Unsuccessful response status code ${response.code()}")
                }
            } else {
                throw ApiException("Response is null")
            }
        } catch (e: Exception) {
            throw ApiException("API error: ${e.message}", e)
        }
    }

}