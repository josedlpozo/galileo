/*
 * The MIT License (MIT)
 *
 * Original Work: Copyright (c) 2015 Danylyk Dmytro
 *
 * Modified Work: Copyright (c) 2015 Rottmann, Jonas
 *
 * Modified Work: Copyright (c) 2018 vicfran
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
package com.josedlpozo.galileo.realm.realmbrowser.browser.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.AppCompatCheckBox
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import android.view.SubMenu
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast

import com.josedlpozo.galileo.R
import com.josedlpozo.galileo.realm.realmbrowser.browser.BrowserContract
import com.josedlpozo.galileo.realm.realmbrowser.browser.BrowserPresenter
import com.josedlpozo.galileo.realm.realmbrowser.helper.DataHolder
import com.josedlpozo.galileo.realm.realmbrowser.view.RealmObjectActivity

import java.lang.reflect.Field
import java.util.AbstractList
import java.util.ArrayList
import java.util.Locale

import io.realm.DynamicRealm
import io.realm.DynamicRealmObject
import io.realm.RealmConfiguration
import io.realm.RealmList
import io.realm.RealmModel

class RealmBrowserActivity : AppCompatActivity(), CompoundButton.OnCheckedChangeListener, BrowserContract.View {

    private lateinit var presenter: BrowserPresenter

    private var dynamicRealm: DynamicRealm? = null
    private lateinit var mAdapter: RealmBrowserAdapter
    private lateinit var textView: TextView
    private lateinit var txtColumn1: TextView
    private lateinit var txtColumn2: TextView
    private lateinit var txtColumn3: TextView
    private lateinit var checkBoxes: Array<AppCompatCheckBox>
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var fab: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.realm_browser_ac_realm_browser)

        val configuration = DataHolder.getInstance().retrieve(DataHolder.DATA_HOLDER_KEY_CONFIG) as RealmConfiguration?
        if (configuration != null) dynamicRealm = DynamicRealm.getInstance(configuration)

        mAdapter = RealmBrowserAdapter {
            presenter.onRowSelected(it)
        }

        val recyclerView = findViewById<RecyclerView>(R.id.realm_browser_recyclerView)
        val layoutManager = LinearLayoutManager(applicationContext)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = mAdapter
        textView = findViewById(R.id.realm_browser_txtIndex)
        txtColumn1 = findViewById(R.id.realm_browser_txtColumn1)
        txtColumn2 = findViewById(R.id.realm_browser_txtColumn2)
        txtColumn3 = findViewById(R.id.realm_browser_txtColumn3)
        fab = findViewById(R.id.realm_browser_fab)
        fab.setOnClickListener { presenter.onNewObjectSelected() }

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.realm_browser_ic_menu_white_24dp)
        }

        drawerLayout = findViewById(R.id.realm_browser_drawer_layout)

        presenter = BrowserPresenter(this)

        if (intent.extras != null && intent.extras!!.containsKey(EXTRAS_DISPLAY_MODE)) {
            presenter.requestForContentUpdate(this.dynamicRealm, intent.extras!!.getInt(EXTRAS_DISPLAY_MODE))
        }

        // TODO: Reenable button when item creation works
        fab.visibility = View.GONE
    }

    override fun onResume() {
        mAdapter.notifyDataSetChanged()
        super.onResume()
    }

    override fun onRetainCustomNonConfigurationInstance(): Any? {
        return presenter
    }

    override fun onDestroy() {
        dynamicRealm?.let { it.close() }
        super.onDestroy()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.realm_browser_menu_browseractivity, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            presenter.onShowMenuSelected()
            return true
        } else if (item.itemId == R.id.realm_browser_action_info) {
            presenter.onInformationSelected()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCheckedChanged(buttonView: CompoundButton, isChecked: Boolean) {
        presenter.onFieldSelectionChanged(buttonView.tag as Int, isChecked)
    }

    override fun showMenu() {
        drawerLayout.openDrawer(GravityCompat.START)
    }

    override fun updateWithRealmObjects(objects: List<DynamicRealmObject>) {
        mAdapter.setRealmList(objects)
        mAdapter.notifyDataSetChanged()
    }

    override fun updateWithFABVisibility(visible: Boolean) {
        fab.visibility = if (visible) View.VISIBLE else View.GONE
    }

    override fun updateWithTitle(title: String) {
        supportActionBar?.let {
            it.title = title
        }
    }

    override fun updateWithFieldList(fields: List<Field>, selectedFieldIndices: Array<Int>) {
        val menu = (findViewById<View>(R.id.realm_browser_navigationView) as NavigationView).menu

        menu.findItem(R.id.realm_browser_menu_fields).subMenu.clear()
        val checkBoxes = arrayOfNulls<AppCompatCheckBox>(fields.size)
        val subMenu = menu.findItem(R.id.realm_browser_menu_fields).subMenu
        for (i in fields.indices) {
            val m = subMenu.add(fields[i].name)
            val cb = AppCompatCheckBox(this)
            cb.setOnCheckedChangeListener(this)
            cb.tag = i
            checkBoxes[i] = cb
            m.actionView = cb
        }

        val selectedFieldList = ArrayList<Field>()
        for (i in selectedFieldIndices) {
            selectedFieldList.add(fields[i])
            checkBoxes[i]?.setOnCheckedChangeListener(null)
            checkBoxes[i]?.isChecked = true
            checkBoxes[i]?.setOnCheckedChangeListener(this)
        }

        updateColumnTitle(selectedFieldList)
        mAdapter.setFieldList(selectedFieldList)
        mAdapter.notifyDataSetChanged()

        this.checkBoxes = checkBoxes.filterNotNull().toTypedArray()

        if (selectedFieldIndices.size >= 3) {
            disableCheckBoxes()
        } else {
            enableCheckboxes()
        }
    }

    override fun showInformation(numberOfRows: Long) {
        Toast.makeText(this, String.format(Locale.getDefault(), "Number of rows: %d", numberOfRows), Toast.LENGTH_SHORT).show()
    }

    override fun goToNewObject(realmModelClass: Class<out RealmModel>, newObject: Boolean) {
        val intent = RealmObjectActivity.getIntent(this, realmModelClass, false)
        startActivity(intent)
    }

    private fun disableCheckBoxes() {
        for (cb in checkBoxes) {
            if (!cb.isChecked) {
                cb.isEnabled = false
            }
        }
    }

    private fun enableCheckboxes() {
        for (cb in checkBoxes) {
            cb.isEnabled = true
        }
    }

    private fun updateColumnTitle(columnsList: List<Field>) {
        textView.text = "#"
        val layoutParams2 = createLayoutParams()
        val layoutParams3 = createLayoutParams()
        if (columnsList.isNotEmpty()) {
            txtColumn1.text = columnsList[0].name

            if (columnsList.size > 1) {
                txtColumn2.text = columnsList[1].name
                layoutParams2.weight = 1f

                if (columnsList.size > 2) {
                    txtColumn3.text = columnsList[2].name
                    layoutParams3.weight = 1f
                } else {
                    layoutParams3.weight = 0f
                }
            } else {
                layoutParams2.weight = 0f
            }
        } else {
            txtColumn1.text = null
        }

        txtColumn2.layoutParams = layoutParams2
        txtColumn3.layoutParams = layoutParams3
    }

    private fun createLayoutParams(): LinearLayout.LayoutParams {
        return LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    companion object {
        private const val EXTRAS_DISPLAY_MODE = "DISPLAY_MODE"

        fun start(context: Context, @BrowserContract.DisplayMode displayMode: Int) {
            val intent = Intent(context, RealmBrowserActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.putExtra(EXTRAS_DISPLAY_MODE, displayMode)
            context.startActivity(intent)
        }
    }
}