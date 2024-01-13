package com.sharmadhiraj.mycurrencyexchange.data.repository

import android.content.SharedPreferences
import dagger.hilt.android.scopes.ActivityScoped
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@ActivityScoped
class ExchangeDataCacheValidator @Inject constructor(
    private val sharedPreferences: SharedPreferences
) {

    private companion object {
        const val PREF_LAST_REFRESH_TIME = "last_refresh_time"
        const val REFRESH_INTERVAL_MINUTES: Long = 30
    }

    fun isCacheValid(): Boolean {
        val lastRefreshTime = sharedPreferences.getLong(PREF_LAST_REFRESH_TIME, 0)
        val currentTime = System.currentTimeMillis()

        return currentTime - lastRefreshTime < TimeUnit.MINUTES.toMillis(REFRESH_INTERVAL_MINUTES)
    }

    fun updateLastRefreshTime() {
        val currentTime = System.currentTimeMillis()
        sharedPreferences.edit().putLong(PREF_LAST_REFRESH_TIME, currentTime).apply()
    }
}