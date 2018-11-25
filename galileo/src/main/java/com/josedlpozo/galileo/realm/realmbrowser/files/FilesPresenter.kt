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
package com.josedlpozo.galileo.realm.realmbrowser.files

import com.josedlpozo.galileo.realm.realmbrowser.files.model.RealmFile
import com.josedlpozo.galileo.realm.realmbrowser.helper.DataHolder
import io.realm.DynamicRealm
import io.realm.RealmConfiguration
import io.realm.exceptions.RealmFileException
import io.realm.exceptions.RealmMigrationNeededException

internal class FilesPresenter(val view: RealmFilesView, private val useCase: FilesUseCase) {

    fun load() {
        useCase {
            view.render(it)
        }
    }

    fun onFileSelected(file: RealmFile) {
        try {
            val config = RealmConfiguration.Builder().name(file.name).build()
            DataHolder.getInstance().save(DataHolder.DATA_HOLDER_KEY_CONFIG, config)
            val realm = DynamicRealm.getInstance(config)
            realm.close()
            view.open(file.name)
        } catch (e: Exception) {
            view.renderError()
        }
    }
}