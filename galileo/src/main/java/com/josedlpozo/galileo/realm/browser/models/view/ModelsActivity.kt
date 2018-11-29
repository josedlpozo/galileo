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
package com.josedlpozo.galileo.realm.browser.models.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v4.content.FileProvider
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.view.Menu
import android.view.MenuItem
import com.josedlpozo.galileo.R
import com.josedlpozo.galileo.realm.browser.browser.RealmBrowserActivity
import com.josedlpozo.galileo.realm.browser.helper.DataHolder
import com.josedlpozo.galileo.realm.browser.models.GetModelsUseCase
import com.josedlpozo.galileo.realm.browser.models.ModelsPresenter
import com.josedlpozo.galileo.realm.browser.models.ModelsView
import com.josedlpozo.galileo.realm.browser.models.model.Asc
import com.josedlpozo.galileo.realm.browser.models.model.Desc
import com.josedlpozo.galileo.realm.browser.models.model.GalileoRealmModel
import com.josedlpozo.galileo.realm.browser.models.model.Sort
import io.realm.RealmConfiguration
import java.io.File

class ModelsActivity : AppCompatActivity(), ModelsView, SearchView.OnQueryTextListener {

    private lateinit var adapter: ModelsAdapter
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var presenter: ModelsPresenter
    private var sortMenuItem: MenuItem? = null
    private lateinit var searchView: SearchView
    private var searchString: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.realm_browser_ac_recycler)

        val actionBar = supportActionBar
        title = intent.getStringExtra(FILE_NAME_KEY)
        actionBar?.setDisplayHomeAsUpEnabled(true)
        swipeRefreshLayout = findViewById(R.id.swiperefresh)
        swipeRefreshLayout.setOnRefreshListener { presenter.load() }
        swipeRefreshLayout.setColorSchemeResources(R.color.realm_browser_dark_purple)

        adapter = ModelsAdapter { file ->
            presenter.onModelSelected(file)
        }
        val recyclerView = findViewById<RecyclerView>(R.id.realm_browser_recycler)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        if (savedInstanceState != null) {
            searchString = savedInstanceState.getString(SEARCH_KEY)
        }

        val realmConfiguration = DataHolder.instance.retrieve(DataHolder.DATA_HOLDER_KEY_CONFIG) as RealmConfiguration?
        if (realmConfiguration == null) {
            finish()
        } else {
            presenter = ModelsPresenter(this, realmConfiguration, GetModelsUseCase(realmConfiguration))
            presenter.load()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_KEY, searchString)
    }

    override fun onRetainCustomNonConfigurationInstance(): Any? {
        return presenter
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.realm_browser_menu_modelsactivity, menu)

        sortMenuItem = menu.findItem(R.id.realm_browser_action_sort)

        val searchMenuItem = menu.findItem(R.id.realm_browser_action_filter)
        searchView = searchMenuItem.actionView as SearchView
        searchView.setOnQueryTextListener(this)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        if (searchString?.isEmpty() == false) {
            (menu.findItem(R.id.realm_browser_action_filter).actionView as SearchView).isIconified = false
            searchView.requestFocus()
            searchView.setQuery(searchString, true)
            searchView.clearFocus()
        }
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.realm_browser_action_sort) {
            presenter.onSortModeChanged()
            return true
        } else if (item.itemId == R.id.realm_browser_action_share) {
            presenter.onShareSelected()
            return true
        } else if (item.itemId == android.R.id.home) {
            finish()
            return true
        } else {
            return super.onOptionsItemSelected(item)
        }
    }

    override fun updateWithModels(filesList: List<GalileoRealmModel>, sort: Sort) {
        adapter.swapList(filesList)
        if (sort is Asc) {
            sortMenuItem?.icon = ContextCompat.getDrawable(this, R.drawable.realm_browser_ic_sort_ascending_white_24dp)
        } else if (sort is Desc) {
            sortMenuItem?.icon = ContextCompat.getDrawable(this, R.drawable.realm_browser_ic_sort_descending_white_24dp)
        }
        if (swipeRefreshLayout.isRefreshing) {
            swipeRefreshLayout.isRefreshing = false
        }
    }

    override fun openBrowser() {
        RealmBrowserActivity.start(this, 0)
    }

    override fun presentShareDialog(path: String) {
        val contentUri = FileProvider.getUriForFile(this, String.format(this.applicationInfo.packageName + ".com.josedlpozo.galileo.provider"),
                                                    File(path))
        val intentShareFile = Intent(Intent.ACTION_SEND)
        intentShareFile.type = "application/*"
        intentShareFile.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        intentShareFile.putExtra(Intent.EXTRA_STREAM, contentUri)
        startActivity(Intent.createChooser(intentShareFile, "Share Realm File"))
    }

    override fun onQueryTextSubmit(query: String): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String): Boolean {
        this.searchString = newText
        presenter.onFilterChanged(newText)
        return true
    }

    companion object {
        private const val SEARCH_KEY = "search"
        private const val FILE_NAME_KEY = "file_name"

        fun getIntent(context: Context, fileName: String): Intent {
            val intent = Intent(context, ModelsActivity::class.java)
            intent.putExtra(FILE_NAME_KEY, fileName)
            return intent
        }
    }
}