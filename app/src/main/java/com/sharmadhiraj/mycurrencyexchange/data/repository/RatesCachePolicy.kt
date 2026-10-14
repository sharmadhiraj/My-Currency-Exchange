package com.sharmadhiraj.mycurrencyexchange.data.repository

import android.content.SharedPreferences
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

/** Tracks when rates were last fetched from the network and whether they are still fresh. */
@Singleton
class RatesCachePolicy @Inject constructor(private val sharedPreferences: SharedPreferences) {

    fun isFresh(): Boolean {
        val lastRefresh = lastRefreshMillis() ?: return false
        return System.currentTimeMillis() - lastRefresh < MAX_AGE_MILLIS
    }

    fun lastRefreshMillis(): Long? {
        return sharedPreferences.getLong(KEY_LAST_REFRESH, 0).takeIf { it > 0 }
    }

    fun markRefreshed() {
        sharedPreferences.edit().putLong(KEY_LAST_REFRESH, System.currentTimeMillis()).apply()
    }

    private companion object {
        const val KEY_LAST_REFRESH = "last_refresh_time"
        val MAX_AGE_MILLIS = TimeUnit.MINUTES.toMillis(30)
    }
}
