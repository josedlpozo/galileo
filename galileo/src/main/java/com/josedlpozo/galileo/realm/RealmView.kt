/*
 * Copyright (C) 2018 vicfran.
 *
 * Modified Work: Copyright (c) 2018 josedlpozo
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
package com.josedlpozo.galileo.realm

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.View
import android.widget.Toast
import com.josedlpozo.galileo.R
import com.josedlpozo.galileo.items.GalileoItem
import com.josedlpozo.galileo.realm.browser.files.FilesPresenter
import com.josedlpozo.galileo.realm.browser.files.FilesUseCase
import com.josedlpozo.galileo.realm.browser.files.RealmFilesView
import com.josedlpozo.galileo.realm.browser.files.RealmFile
import com.josedlpozo.galileo.realm.browser.files.FilesAdapter
import com.josedlpozo.galileo.realm.browser.models.view.ModelsActivity
import java.util.*

class RealmView @JvmOverloads internal constructor(context: Context, val attr: AttributeSet? = null, defStyleAttr: Int = 0)
    : RecyclerView(context, attr, defStyleAttr), GalileoItem, RealmFilesView {

    override val name: String = "RealmBrowser"

    override val view: View = this

    override val icon: Int = R.drawable.realm_browser_ic_rb

    override fun snapshot(): String = presenter.generateSnapshoot()

    private val presenter: FilesPresenter = FilesPresenter(this, FilesUseCase(context))

    private val filesAdapter: FilesAdapter

    init {
        filesAdapter = FilesAdapter(ArrayList()) { presenter.onFileSelected(it) }
        adapter = filesAdapter
        layoutManager = LinearLayoutManager(context)
        presenter.load()
    }

    override fun render(files: List<RealmFile>) {
        filesAdapter.swapList(files)
        if (files.isEmpty()) {
            // TODO show empty list placeholder
        }
    }

    override fun renderError() {
        Toast.makeText(context, R.string.realm_browser_open_error, Toast.LENGTH_SHORT).show()
    }

    override fun open(name: String) {
        ModelsActivity.getIntent(context, name).also(context::startActivity)
    }
}