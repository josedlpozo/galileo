/*
 * Copyright (C) 2015 Square, Inc, 2017 Jeff Gilfelt, 2018 josedlpozo.
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
package com.josedlpozo.galileo.chuck

import com.josedlpozo.galileo.chuck.data.HttpTransaction
import com.josedlpozo.galileo.chuck.data.HttpTransaction.Companion.toHttpHeaderList
import com.josedlpozo.galileo.chuck.data.HttpTransactionRepository
import okhttp3.Headers
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.internal.http.HttpHeaders
import okio.Buffer
import okio.BufferedSource
import okio.GzipSource
import okio.Okio
import java.io.EOFException
import java.io.IOException
import java.nio.charset.Charset
import java.nio.charset.UnsupportedCharsetException
import java.util.*
import java.util.concurrent.TimeUnit

object GalileoChuckInterceptor : Interceptor {

    private const val maxContentLength = 250000L
    private val UTF8 = Charset.forName("UTF-8")
    private val repository: HttpTransactionRepository = HttpTransactionRepository

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        val requestBody = request.body()
        val hasRequestBody = requestBody != null

        val transactionId = System.nanoTime()
        val requestDate = Date()

        val method = request.method()
        val url = request.url()

        val requestContentType = requestBody?.contentType().toString()
        val requestContentLength = requestBody?.contentLength()

        var requestBodyIsPlainText = !bodyHasUnsupportedEncoding(request.headers())

        var requestBodyText: String? = null
        if (hasRequestBody && requestBodyIsPlainText) {
            val source = getNativeSource(Buffer(), bodyGzipped(request.headers()))
            val buffer = source.buffer()
            requestBody!!.writeTo(buffer)
            var charset: Charset? = UTF8
            val contentType = requestBody.contentType()
            if (contentType != null) {
                charset = contentType.charset(UTF8)
            }
            if (isPlaintext(buffer)) {
                requestBodyText = readFromBuffer(buffer, charset)
            } else {
                requestBodyIsPlainText = false
            }
        }


        val startNs = System.nanoTime()
        val response: Response
        try {
            response = chain.proceed(request)
        } catch (e: Exception) {
            throw e
        }

        val tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs)

        val responseBody = response.body()

        val requestHeaders = toHttpHeaderList(response.request().headers())
        val responseDate = Date()
        val protocol = response.protocol().toString()
        val responseCode = response.code()
        val responseMessage = response.message()

        val responseContentLength = responseBody?.contentLength()
        val responseContentType = responseBody?.contentType().toString()

        val responseHeaders = toHttpHeaderList(response.headers())

        var responseBodyIsPlainText = !bodyHasUnsupportedEncoding(response.headers())
        var responseBodyText: String? = null
        if (HttpHeaders.hasBody(response) && responseBodyIsPlainText) {
            val source = getNativeSource(response)
            source?.request(java.lang.Long.MAX_VALUE)
            val buffer = source?.buffer()
            var charset: Charset? = UTF8
            val contentType = responseBody?.contentType()
            if (contentType != null) {
                try {
                    charset = contentType.charset(UTF8)
                } catch (e: UnsupportedCharsetException) {
                    return response
                }
            }
            if (isPlaintext(buffer)) {
                responseBodyText = readFromBuffer(buffer?.clone(), charset)
            } else {
                responseBodyIsPlainText = false
            }
        }

        val transaction = HttpTransaction(transactionId, requestDate, responseDate, tookMs,
                protocol, method, url, requestContentLength, requestContentType,
                requestHeaders, requestBodyText, requestBodyIsPlainText, responseCode,
                responseMessage, responseContentLength, responseContentType,
                responseHeaders, responseBodyText, responseBodyIsPlainText)
        repository.add(transaction)

        return response
    }

    /**
     * Returns true if the body in question probably contains human readable text. Uses a small sample
     * of code points to detect unicode control characters commonly used in binary file signatures.
     */
    private fun isPlaintext(buffer: Buffer?): Boolean {
        if (buffer == null) return false
        try {
            val prefix = Buffer()
            val byteCount = if (buffer.size() < 64) buffer.size() else 64
            buffer.copyTo(prefix, 0, byteCount)
            for (i in 0..15) {
                if (prefix.exhausted()) {
                    break
                }
                val codePoint = prefix.readUtf8CodePoint()
                if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                    return false
                }
            }
            return true
        } catch (e: EOFException) {
            return false // Truncated UTF-8 sequence.
        }

    }

    private fun bodyHasUnsupportedEncoding(headers: Headers): Boolean {
        val contentEncoding = headers.get("Content-Encoding")
        return contentEncoding != null &&
                !contentEncoding.equals("identity", ignoreCase = true) &&
                !contentEncoding.equals("gzip", ignoreCase = true)
    }

    private fun bodyGzipped(headers: Headers): Boolean {
        val contentEncoding = headers.get("Content-Encoding")
        return "gzip".equals(contentEncoding, ignoreCase = true)
    }

    private fun readFromBuffer(buffer: Buffer?, charset: Charset?): String {
        if (buffer == null) return ""
        val bufferSize = buffer.size()
        val maxBytes = Math.min(bufferSize, maxContentLength)
        var body = ""
        try {
            body = buffer.readString(maxBytes, charset!!)
        } catch (e: EOFException) {
            body += "\\n\\n--- Unexpected end of content ---"
        }

        return body
    }

    private fun getNativeSource(input: BufferedSource, isGzipped: Boolean): BufferedSource =
            if (isGzipped) {
                val source = GzipSource(input)
                Okio.buffer(source)
            } else {
                input
            }

    @Throws(IOException::class)
    private fun getNativeSource(response: Response): BufferedSource? {
        if (bodyGzipped(response.headers())) {
            val source = response.peekBody(maxContentLength).source()
            if (source.buffer().size() < maxContentLength) {
                return getNativeSource(source, true)
            }
        }
        return response.body()?.source()
    }

}