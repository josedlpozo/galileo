/*
 * Copyright (C) 2017 Jeff Gilfelt, 2018 josedlpozo.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.josedlpozo.galileo.chuck.data

import com.josedlpozo.galileo.chuck.support.FormatUtils
import okhttp3.Headers
import okhttp3.HttpUrl
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

class HttpTransaction(val id: Long, private val requestDate: Date, private val responseDate: Date,
                      private val tookMs: Long, val protocol: String, val method: String,
                      url: HttpUrl, private val requestContentLength: Long?,
                      private val requestContentType: String?, val requestHeaders: List<HttpHeader>,
                      val requestBody: String?, private val requestBodyIsPlainText: Boolean,
                      val responseCode: Int, private val responseMessage: String,
                      private val responseContentLength: Long?,
                      private val responseContentType: String?, val responseHeaders: List<HttpHeader>,
                      private var responseBody: String?, private val responseBodyIsPlainText: Boolean) {

    enum class Status {
        Requested,
        Complete,
        Failed
    }

    val url: String = url.scheme() + "://" + url.host() + url.encodedPath()
    val host: String = url.host()
    val path: String = url.encodedPath()
    val queryParams: Map<String, String> = url.queryMap()
    private val scheme: String = url.scheme()

    val formattedRequestBody: String
        get() = formatBody(requestBody, requestContentType) ?: ""

    val formattedResponseBody: String
        get() = formatBody(responseBody, responseContentType) ?: ""

    val formattedQueryParams: String
        get() = queryParams.map { "<b>${it.key}</b>: ${it.value}" }.joinToString("<br />")

    val status: Status
        get() = Status.Complete

    val requestStartTimeString: String?
        get() = TIME_ONLY_FMT.format(requestDate)

    val requestDateString: String
        get() = TIME_ONLY_FMT.format(requestDate)

    val responseDateString: String
        get() = TIME_ONLY_FMT.format(responseDate)

    val durationString: String
        get() = "$tookMs ms"

    val requestSizeString: String
        get() = formatBytes(requestContentLength ?: 0)

    val responseSizeString: String?
        get() = if (responseContentLength != null) formatBytes(responseContentLength) else null

    val totalSizeString: String
        get() {
            val reqBytes = requestContentLength ?: 0
            val resBytes = responseContentLength ?: 0
            return formatBytes(reqBytes + resBytes)
        }

    val responseSummaryText: String?
        get() = when (status) {
            HttpTransaction.Status.Requested -> null
            else -> responseCode.toString() + " " + responseMessage
        }

    val isSsl: Boolean
        get() = scheme.toLowerCase() == "https"

    fun requestBodyIsPlainText(): Boolean {
        return requestBodyIsPlainText
    }

    fun responseBodyIsPlainText(): Boolean = responseBodyIsPlainText

    fun getRequestHeadersString(withMarkup: Boolean): String = FormatUtils.formatHeaders(requestHeaders, withMarkup)

    fun getResponseHeadersString(withMarkup: Boolean): String = FormatUtils.formatHeaders(responseHeaders, withMarkup)

    private fun formatBody(body: String?, contentType: String?): String? {
        return if (contentType != null && contentType.toLowerCase().contains("json")) {
            FormatUtils.formatJson(body!!)
        } else if (contentType != null && contentType.toLowerCase().contains("xml")) {
            FormatUtils.formatXml(body!!)
        } else {
            body
        }
    }

    private fun formatBytes(bytes: Long): String = FormatUtils.formatByteCount(bytes)

    companion object {
        private val TIME_ONLY_FMT = SimpleDateFormat("HH:mm:ss", Locale.US)

        fun toHttpHeaderList(headers: Headers): List<HttpHeader> {
            val httpHeaders = ArrayList<HttpHeader>()
            var i = 0
            val count = headers.size()
            while (i < count) {
                httpHeaders.add(HttpHeader(headers.name(i), headers.value(i)))
                i++
            }
            return httpHeaders
        }
    }

    private fun HttpUrl.queryMap(): Map<String, String> {
        val queryMap = LinkedHashMap<String, String>()
        return try {
            val pairs = query()?.split("&".toRegex())?.dropLastWhile { it.isEmpty() }?.toTypedArray()
            if (pairs == null) queryMap
            else {
                for (pair in pairs) {
                    val idx = pair.indexOf("=")
                    queryMap[pair.substring(0, idx)] = pair.substring(idx + 1)
                }
                queryMap
            }
        } catch (e: Exception) {
            emptyMap()
        }
    }
}