package com.sharmadhiraj.mycurrencyexchange.data.repository

import android.content.SharedPreferences
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.concurrent.TimeUnit

class RatesCachePolicyTest {

    private val editor: SharedPreferences.Editor = mockk(relaxed = true) {
        every { putLong(any(), any()) } returns this
    }
    private val preferences: SharedPreferences = mockk {
        every { edit() } returns editor
    }
    private val policy = RatesCachePolicy(preferences)

    private fun lastRefreshedMinutesAgo(minutes: Long) {
        every { preferences.getLong(any(), any()) } returns
            System.currentTimeMillis() - TimeUnit.MINUTES.toMillis(minutes)
    }

    @Test
    fun `rates refreshed 29 minutes ago are fresh`() {
        lastRefreshedMinutesAgo(29)

        assertTrue(policy.isFresh())
    }

    @Test
    fun `rates refreshed 31 minutes ago are not fresh`() {
        lastRefreshedMinutesAgo(31)

        assertFalse(policy.isFresh())
    }

    @Test
    fun `never refreshed means not fresh and no timestamp`() {
        every { preferences.getLong(any(), any()) } returns 0L

        assertFalse(policy.isFresh())
        assertNull(policy.lastRefreshMillis())
    }

    @Test
    fun `lastRefreshMillis returns the stored timestamp`() {
        every { preferences.getLong(any(), any()) } returns 1234L

        assertEquals(1234L, policy.lastRefreshMillis())
    }

    @Test
    fun `markRefreshed stores the current time`() {
        policy.markRefreshed()

        verify { editor.putLong(any(), more(0L)) }
        verify { editor.apply() }
    }
}
