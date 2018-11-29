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
package com.josedlpozo.galileo.realm.browser.models

import com.josedlpozo.galileo.realm.browser.helper.DataHolder
import com.josedlpozo.galileo.realm.browser.models.model.Asc
import com.josedlpozo.galileo.realm.browser.models.model.GalileoRealmModel
import com.josedlpozo.galileo.realm.browser.models.model.Sort
import com.josedlpozo.galileo.realm.browser.models.model.inverse
import io.realm.RealmConfiguration

internal class ModelsPresenter(private val view: ModelsView, private val realmConfiguration: RealmConfiguration,
                               private val modelsUseCase: GetModelsUseCase) {

    private var filter: String = ""
    private var sort: Sort = Asc

    fun load() {
        modelsUseCase.all(filter, sort) {
            view.updateWithModels(it, sort)
        }
    }

    fun onSortModeChanged() {
        sort = sort.inverse()
        load()
    }

    fun onFilterChanged(filter: String) {
        this.filter = filter
        load()
    }

    fun onModelSelected(item: GalileoRealmModel) {
        DataHolder.instance.save(DataHolder.DATA_HOLDER_KEY_CLASS, item.klass)
        view.openBrowser()
    }

    fun onShareSelected() {
        view.presentShareDialog(realmConfiguration.path)
    }
}