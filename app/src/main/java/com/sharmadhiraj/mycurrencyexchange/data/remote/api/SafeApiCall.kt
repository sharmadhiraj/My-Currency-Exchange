package com.sharmadhiraj.mycurrencyexchange.data.remote.api

import retrofit2.Response
import kotlin.coroutines.cancellation.CancellationException

suspend fun <T> safeApiCall(call: suspend () -> Response<T>): T {
    val response = try {
        call()
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        throw ApiException("Request failed: ${e.message}", e)
    }
    if (!response.isSuccessful) {
        throw ApiException("Unsuccessful response status code ${response.code()}")
    }
    return response.body() ?: throw ApiException("Response body is empty")
}
