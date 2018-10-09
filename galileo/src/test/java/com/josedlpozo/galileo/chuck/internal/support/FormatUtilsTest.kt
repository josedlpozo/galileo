package com.josedlpozo.galileo.chuck.internal.support

import com.josedlpozo.galileo.chuck.internal.data.HttpHeader
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import org.junit.Test

class FormatUtilsTest {

    @Test
    fun `given empty headers list, when formatting, then returns empty string`() {
        val result = FormatUtils.formatHeaders(emptyList(), true)

        assertTrue(result.isEmpty())
    }

    @Test
    fun `given null headers list, when formatting, then returns empty string`() {
        val result = FormatUtils.formatHeaders(null, true)

        assertTrue(result.isEmpty())
    }

    @Test
    fun `given one header in the list, when formatting without markup, then returns Header double dot value`() {
        val header = HttpHeader("Header", "value")
        val result = FormatUtils.formatHeaders(listOf(header), false)

        assertEquals("${header.name}: ${header.value}\n", result)
    }

    @Test
    fun `given two headers in the list, when formatting without markup, then returns properly`() {
        val header1 = HttpHeader("Header", "value")
        val header2 = HttpHeader("Header1", "value1")
        val result = FormatUtils.formatHeaders(listOf(header1, header2), false)

        assertEquals("${header1.name}: ${header1.value}\n${header2.name}: ${header2.value}\n", result)
    }

    @Test
    fun `given one header in the list, when formatting with markup, then returns Header double dot value`() {
        val header = HttpHeader("Header", "value")
        val result = FormatUtils.formatHeaders(listOf(header), true)

        assertEquals("<b>${header.name}: </b>${header.value}<br />", result)
    }

    @Test
    fun `given two headers in the list, when formatting with markup, then returns properly`() {
        val header1 = HttpHeader("Header", "value")
        val header2 = HttpHeader("Header1", "value1")
        val result = FormatUtils.formatHeaders(listOf(header1, header2), true)

        assertEquals("<b>${header1.name}: </b>${header1.value}<br /><b>${header2.name}: </b>${header2.value}<br />", result)
    }
}