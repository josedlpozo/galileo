package com.josedlpozo.galileo.chuck.internal.support

import com.josedlpozo.galileo.chuck.internal.data.HttpHeader
import com.josedlpozo.galileo.chuck.internal.data.HttpTransaction
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import okhttp3.HttpUrl
import org.junit.Test
import java.util.*
import kotlin.math.exp

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

    @Test
    fun `given a HttpTransaction, when getShareText, then returns properly`() {
        val transaction = HttpTransaction(0).apply {
            setUrl(HTTP_URL)
            method = HTTP_METHOD
            protocol = HTTP_PROTOCOL
            responseCode = 200
            setResponseMessage(HTTP_RESPONSE_MESSAGE)
            setRequestDate(HTTP_REQUEST_DATE)
            setResponseDate(HTTP_RESPONSE_DATE)
            setTookMs(HTTP_DURATION)
            setRequestContentLength(HTTP_REQUEST_SIZE)
            setResponseContentLength(HTTP_REQUEST_SIZE)
            requestBody = HTTP_BODY
            setResponseBody(HTTP_BODY)
        }

        val result = FormatUtils.getShareText(transaction)

        val expected = """
            URL: $HTTP_URL
            Method: $HTTP_METHOD
            Protocol: $HTTP_PROTOCOL
            Status: $HTTP_STATUS
            Response: $HTTP_RESPONSE_SUMMARY
            SSL: Yes
            Request time: $HTTP_REQUEST_DATE
            Response time: $HTTP_RESPONSE_DATE
            Duration: $HTTP_DURATION ms

            Request size: $HTTP_REQUEST_SIZE B
            Response size: $HTTP_RESPONSE_SIZE B
            Total size: 1.4 kB

            ---------- REQUEST ----------


            $HTTP_BODY

            ---------- RESPONSE ----------


            $HTTP_BODY
        """.trimIndent()

        assertEquals(expected, result)
    }

    @Test
    fun `given 8 bytes, when formatting, then returns 8 B`() {
        assertEquals("8 B", FormatUtils.formatByteCount(8))
    }

    @Test
    fun `given 1400 bytes, when formatting, then returns 1 dot 4 kB`() {
        assertEquals("1.4 kB", FormatUtils.formatByteCount(1400))
    }

    companion object {
        val HTTP_URL = HttpUrl.get("https://localhost")
        val HTTP_METHOD = "get"
        val HTTP_PROTOCOL = "http2"
        val HTTP_STATUS = "Complete"
        val HTTP_RESPONSE_MESSAGE = "message"
        val HTTP_RESPONSE_SUMMARY = "200 $HTTP_RESPONSE_MESSAGE"
        val HTTP_REQUEST_DATE = Date(0)
        val HTTP_RESPONSE_DATE = Date(0)
        val HTTP_DURATION = 7.toLong()
        val HTTP_REQUEST_SIZE = 700.toLong()
        val HTTP_RESPONSE_SIZE = 700.toLong()
        val HTTP_BODY = "body"
    }
}