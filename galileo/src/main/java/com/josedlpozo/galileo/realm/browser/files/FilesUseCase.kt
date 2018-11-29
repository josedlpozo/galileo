/*
 * The MIT License (MIT)
 *
 * Original Work: Copyright (c) 2015 Danylyk Dmytro
 *
 * Modified Work: Copyright (c) 2015 Rottmann, Jonas
 *
 * Modified Work: Copyright (c) 2018 vicfran
 *
 * Modified Work: Copyright (c) 2018 josedlpozo
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.josedlpozo.galileo.realm.browser.files

import android.content.Context
import android.text.format.Formatter
import io.realm.DynamicRealm
import io.realm.RealmConfiguration
import java.io.File

internal class FilesUseCase(private val context: Context) {

    private val ignoreFilesExtensions = setOf("log", "log_a", "log_b", "lock", "management", "temp", "txt")

    operator fun invoke(callback: (List<RealmFile>) -> Unit) {
        val dataDir = File(context.applicationInfo.dataDir, "files")
        val files = dataDir.listFiles().filter { !ignoreFilesExtensions.contains(it.name.substringAfterLast(".", "")) }
                .map {
            val fileName = it.name
            try {
                val config = RealmConfiguration.Builder().name(fileName).build()
                DynamicRealm.getInstance(config).close()
                RealmFile(fileName, Formatter.formatShortFileSize(context, it.length()), it.length())
            } catch (exception: Exception) {
                null
            }
        }.filterNotNull()
        callback(files)
    }
}
