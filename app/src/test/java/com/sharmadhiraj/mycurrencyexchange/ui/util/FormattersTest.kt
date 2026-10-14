package com.sharmadhiraj.mycurrencyexchange.ui.util

import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import java.util.Locale

class FormattersTest {

    private val originalLocale = Locale.getDefault()

    @Before
    fun setUp() = Locale.setDefault(Locale.US)

    @After
    fun tearDown() = Locale.setDefault(originalLocale)

    @Test
    fun `parseAmount accepts dot and comma decimals`() {
        assertEquals(10.5, parseAmount("10.5")!!, 0.0)
        assertEquals(10.5, parseAmount(" 10,5 ")!!, 0.0)
    }

    @Test
    fun `parseAmount rejects blank, text, and negatives`() {
        assertNull(parseAmount(""))
        assertNull(parseAmount("abc"))
        assertNull(parseAmount("-1"))
    }

    @Test
    fun `formatAmount uses two decimals for normal amounts`() {
        assertEquals("1,234.50", formatAmount(1234.5))
        assertEquals("0.00", formatAmount(0.0))
    }

    @Test
    fun `formatAmount keeps precision for tiny amounts`() {
        assertEquals("0.000025", formatAmount(0.000025))
    }
}
