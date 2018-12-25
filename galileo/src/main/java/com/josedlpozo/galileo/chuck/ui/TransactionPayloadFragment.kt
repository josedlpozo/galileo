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

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.SearchView
import android.text.Html
import android.text.TextUtils
import android.view.*
import com.josedlpozo.galileo.R
import com.josedlpozo.galileo.chuck.data.HttpTransaction
import com.josedlpozo.galileo.chuck.data.HttpTransactionRepository
import kotlinx.android.synthetic.main.chuck_fragment_transaction_payload.*
import com.josedlpozo.galileo.chuck.support.SearchHighlighter


internal class TransactionPayloadFragment : Fragment(), SearchView.OnQueryTextListener {

    private var type: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        type = arguments?.getInt(ARG_TYPE) ?: 0
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.chuck_fragment_transaction_payload, container, false)
                    .also { if (type == TYPE_RESPONSE) setHasOptionsMenu(true) else setHasOptionsMenu(false) }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val id = arguments?.getLong(TRANSACTION_ID) ?: 0
        val transaction = HttpTransactionRepository.find(id)
        transaction?.let {
            bind(it)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        if (type == TYPE_RESPONSE) {
            val searchMenuItem = menu.findItem(R.id.search)
            searchMenuItem.isVisible = true
            val searchView = searchMenuItem.actionView as SearchView
            searchView.setOnQueryTextListener(this)
            searchView.setIconifiedByDefault(true)
        }

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onQueryTextSubmit(query: String): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String): Boolean {
        val bodyText = body.text.toString()
        if (newText.trim().isNotEmpty() && bodyText.contains(newText.trim()))
            body.text = SearchHighlighter.format(bodyText, newText)
        else
            body.text = bodyText
        return true
    }

    private fun bind(transaction: HttpTransaction) {
        val transactionHeaders = when (type) {
            TYPE_REQUEST -> transaction.getRequestHeadersString(true)
            TYPE_RESPONSE -> transaction.getResponseHeadersString(true)
            else -> ""
        }

        val transactionQuery = when(type) {
            TYPE_REQUEST -> transaction.formattedQueryParams
            else -> ""
        }

        val transactionBody = when(type) {
            TYPE_REQUEST -> transaction.formattedRequestBody
            TYPE_RESPONSE -> transaction.formattedResponseBody
            else -> ""
        }

        val bodyIsPlainText = when(type) {
            TYPE_REQUEST -> transaction.requestBodyIsPlainText()
            TYPE_RESPONSE -> transaction.responseBodyIsPlainText()
            else -> true
        }

        val headersVisibility = if (TextUtils.isEmpty(transactionHeaders)) View.GONE else View.VISIBLE
        headersTitle.visibility = headersVisibility
        headers.visibility = headersVisibility
        headers.text = Html.fromHtml(transactionHeaders)

        val queryVisibility = if (TextUtils.isEmpty(transactionQuery)) View.GONE else View.VISIBLE
        queryTitle.visibility = queryVisibility
        query.visibility = queryVisibility
        query.text = Html.fromHtml(transactionQuery)

        val bodyVisibility = if (TextUtils.isEmpty(transactionBody)) View.GONE else View.VISIBLE
        bodyTitle.visibility = bodyVisibility
        if (!bodyIsPlainText) {
            body.text = getString(R.string.chuck_body_omitted)
        } else {
            body.text = transactionBody
        }
    }

    companion object {
        const val TYPE_REQUEST = 0
        const val TYPE_RESPONSE = 1

        private const val ARG_TYPE = "type"
        private const val TRANSACTION_ID = "id"

        fun newInstance(type: Int, transactionId: Long): TransactionPayloadFragment = TransactionPayloadFragment().apply {
            val bundle = Bundle().apply {
                putInt(ARG_TYPE, type)
                putLong(TRANSACTION_ID, transactionId)
            }

            arguments = bundle
        }
    }
}