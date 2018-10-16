/*
 * Copyright (C) 2017 Jeff Gilfelt.
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
package com.josedlpozo.galileo.chuck.internal.support

import com.google.gson.JsonParser
import com.josedlpozo.galileo.chuck.internal.data.HttpHeader
import com.josedlpozo.galileo.chuck.internal.data.HttpTransaction
import org.xml.sax.InputSource
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.util.*
import javax.xml.transform.OutputKeys
import javax.xml.transform.sax.SAXSource
import javax.xml.transform.sax.SAXTransformerFactory
import javax.xml.transform.stream.StreamResult

object FormatUtils {

    fun formatHeaders(httpHeaders: List<HttpHeader>?, withMarkup: Boolean): String =
            httpHeaders?.map {
                (if (withMarkup) "<b>" else "") + it.name + ": " + (if (withMarkup) "</b>" else "") +
                        it.value + if (withMarkup) "<br />" else "\n"
            }?.joinToString(separator = "") { it } ?: ""

    fun formatByteCount(bytes: Long): String {
        val unit = 1000
        if (bytes < unit) return bytes.toString() + " B"
        val exp = (Math.log(bytes.toDouble()) / Math.log(unit.toDouble())).toInt()
        val pre = ("kMGTPE")[exp - 1]
        return String.format(Locale.US, "%.1f %sB", bytes / Math.pow(unit.toDouble(), exp.toDouble()), pre)
    }

    fun formatJson(json: String): String = try {
        val jp = JsonParser()
        val je = jp.parse(json)
        JsonConvertor.getInstance().toJson(je)
    } catch (e: Exception) {
        json
    }

    fun formatXml(xml: String): String = try {
        val serializer = SAXTransformerFactory.newInstance().newTransformer()
        serializer.setOutputProperty(OutputKeys.INDENT, "yes")
        serializer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2")
        val xmlSource = SAXSource(InputSource(ByteArrayInputStream(xml.toByteArray())))
        val res = StreamResult(ByteArrayOutputStream())
        serializer.transform(xmlSource, res)
        String((res.outputStream as ByteArrayOutputStream).toByteArray())
    } catch (e: Exception) {
        xml
    }

    fun getShareText(transaction: HttpTransaction): String = """
        URL: ${transaction.url}
        Method: ${transaction.method}
        Protocol: ${transaction.protocol}
        Status: ${transaction.status}
        Response: ${transaction.responseSummaryText}
        SSL: ${if (transaction.isSsl) "Yes" else "No"}
        Request time: ${transaction.requestDateString}
        Response time: ${transaction.responseDateString}
        Duration: ${transaction.durationString}

        Request size: ${transaction.requestSizeString}
        Response size: ${transaction.responseSizeString}
        Total size: ${transaction.totalSizeString}

        ---------- REQUEST ----------

        ${formatHeaders(transaction.requestHeaders, false).let { if (it.isEmpty()) "" else "$it \n" }}
        ${if (transaction.requestBodyIsPlainText()) transaction.formattedRequestBody else "(encoded or binary body omitted)"}

        ---------- RESPONSE ----------

        ${formatHeaders(transaction.responseHeaders, false).let { if (it.isEmpty()) "" else "$it \n" }}
        ${if (transaction.responseBodyIsPlainText()) transaction.formattedResponseBody else "(encoded or binary body omitted)"}
    """.trimIndent()

    fun getShareCurlCommand(transaction: HttpTransaction): String {
        var compressed = false
        var curlCmd = "curl"
        curlCmd += " -X " + transaction.method
        val headers = transaction.requestHeaders
        var i = 0
        val count = headers?.size ?: 0
        while (i < count) {
            val name = headers[i].name
            val value = headers[i].value
            if ("Accept-Encoding".equals(name, ignoreCase = true) && "gzip".equals(value, ignoreCase = true)) {
                compressed = true
            }
            curlCmd += " -H \"$name: $value\""
            i++
        }
        val requestBody = transaction.requestBody
        if (requestBody != null && requestBody.isNotEmpty()) {
            // try to keep to a single line and use a subshell to preserve any line breaks
            curlCmd += " --data $'" + requestBody.replace("\n", "\\n") + "'"
        }
        curlCmd += (if (compressed) " --compressed " else " ") + transaction.url
        return curlCmd
    }

}