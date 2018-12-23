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
package com.josedlpozo.galileo.chuck.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem

import com.josedlpozo.galileo.R
import com.josedlpozo.galileo.chuck.data.HttpTransaction
import com.josedlpozo.galileo.chuck.data.HttpTransactionRepository
import com.josedlpozo.galileo.chuck.support.FormatUtils
import kotlinx.android.synthetic.main.chuck_activity_transaction.*

import java.util.ArrayList

class TransactionActivity : AppCompatActivity() {

    private lateinit var transaction: HttpTransaction

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.chuck_activity_transaction)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val transactionId = intent.getLongExtra(ARG_TRANSACTION_ID, 0)
        val transactionFound = HttpTransactionRepository.find(transactionId)
        if (transactionFound == null) {
            finish()
        } else {
            transaction = transactionFound
        }

        renderTitle()
        setupViewPager(viewPager)
        tabLayout.setupWithViewPager(viewPager)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.chuck_transaction, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when {
        item.itemId == R.id.share_text -> {
            share(FormatUtils.getShareText(transaction))
            true
        }
        item.itemId == R.id.share_curl -> {
            share(FormatUtils.getShareCurlCommand(transaction))
            true
        }
        item.itemId == android.R.id.home -> {
            finish()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    private fun renderTitle() {
        title = transaction.method + " " + transaction.path
    }

    private fun setupViewPager(viewPager: ViewPager) {
        val adapter = Adapter(supportFragmentManager)
        adapter.addFragment(TransactionOverviewFragment.newInstance(transaction.id), getString(R.string.chuck_overview))
        adapter.addFragment(TransactionPayloadFragment.newInstance(TransactionPayloadFragment.TYPE_REQUEST, transaction.id), getString(R.string.chuck_request))
        adapter.addFragment(TransactionPayloadFragment.newInstance(TransactionPayloadFragment.TYPE_RESPONSE, transaction.id), getString(R.string.chuck_response))
        viewPager.adapter = adapter
    }

    private fun share(content: String) {
        val sendIntent = Intent()
        sendIntent.action = Intent.ACTION_SEND
        sendIntent.putExtra(Intent.EXTRA_TEXT, content)
        sendIntent.type = "text/plain"
        startActivity(Intent.createChooser(sendIntent, null))
    }

    internal class Adapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
        private val fragments: MutableList<Fragment> = ArrayList()
        private val fragmentTitles = ArrayList<String>()

        fun addFragment(fragment: Fragment, title: String) {
            fragments.add(fragment)
            fragmentTitles.add(title)
        }

        override fun getItem(position: Int): Fragment {
            return fragments[position]
        }

        override fun getCount(): Int {
            return fragments.size
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return fragmentTitles[position]
        }
    }

    companion object {
        private const val ARG_TRANSACTION_ID = "transaction_id"

        fun start(context: Context, transactionId: Long) = Intent(context, TransactionActivity::class.java).apply {
            putExtra(ARG_TRANSACTION_ID, transactionId)
        }.also(context::startActivity)
    }
}