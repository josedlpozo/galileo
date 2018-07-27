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
package com.readystatesoftware.chuck.internal.ui

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.readystatesoftware.chuck.R
import com.readystatesoftware.chuck.internal.data.HttpTransaction
import com.readystatesoftware.chuck.internal.data.HttpTransactionRepository
import com.readystatesoftware.chuck.internal.ui.TransactionListFragment.OnListFragmentInteractionListener
import java.util.*

internal class TransactionAdapter(context: Context, private val listener: OnListFragmentInteractionListener?) : RecyclerView.Adapter<TransactionAdapter.ViewHolder>() {
    private var list: List<HttpTransaction> = listOf()

    private val colorDefault: Int = ContextCompat.getColor(context, R.color.chuck_status_default)
    private val colorRequested: Int = ContextCompat.getColor(context, R.color.chuck_status_requested)
    private val colorError: Int = ContextCompat.getColor(context, R.color.chuck_status_error)
    private val color500: Int = ContextCompat.getColor(context, R.color.chuck_status_500)
    private val color400: Int = ContextCompat.getColor(context, R.color.chuck_status_400)
    private val color300: Int = ContextCompat.getColor(context, R.color.chuck_status_300)

    init {
        list = ArrayList()

        HttpTransactionRepository.data.observe({ object: Lifecycle() {
            override fun addObserver(observer: LifecycleObserver) {

            }

            override fun removeObserver(observer: LifecycleObserver) {

            }

            override fun getCurrentState(): State = State.INITIALIZED

        } }, {
            if (it == null) return@observe

            refresh(it.toList())
        })
    }

    private fun refresh(updatedList: List<HttpTransaction>) {
        val diffUtil = object : DiffUtil.Callback() {

            override fun getOldListSize(): Int {
                return list.size
            }

            override fun getNewListSize(): Int {
                return updatedList.size
            }

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                val oldItem = list[oldItemPosition]
                val newItem = list[newItemPosition]
                return if (oldItem == null || newItem == null) false else oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                val oldItem = list[oldItemPosition]
                val newItem = list[newItemPosition]
                return if (oldItem == null) false else oldItem == newItem
            }
        }

        val result = DiffUtil.calculateDiff(diffUtil, true)
        result.dispatchUpdatesTo(this)
        list = ArrayList(updatedList)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val transaction = list[position]
        holder.path.text = transaction.method + " " + transaction.path
        holder.host.text = transaction.host
        holder.start.text = transaction.requestStartTimeString
        holder.ssl.visibility = if (transaction.isSsl) View.VISIBLE else View.GONE
        if (transaction.status == HttpTransaction.Status.Complete) {
            holder.code.text = transaction.responseCode.toString()
            holder.duration.text = transaction.durationString
            holder.size.text = transaction.totalSizeString
        } else {
            holder.code.text = null
            holder.duration.text = null
            holder.size.text = null
        }
        if (transaction.status == HttpTransaction.Status.Failed) {
            holder.code.text = "!!!"
        }
        setStatusColor(holder, transaction)
        holder.transaction = transaction
        holder.view.setOnClickListener {
            if (null != this@TransactionAdapter.listener) {
                this@TransactionAdapter.listener.onListFragmentInteraction(holder.transaction)
            }
        }
    }

    private fun setStatusColor(holder: ViewHolder, transaction: HttpTransaction) {
        val color: Int = when {
            transaction.status == HttpTransaction.Status.Failed -> colorError
            transaction.status == HttpTransaction.Status.Requested -> colorRequested
            transaction.responseCode >= 500 -> color500
            transaction.responseCode >= 400 -> color400
            transaction.responseCode >= 300 -> color300
            else -> colorDefault
        }
        holder.code.setTextColor(color)
        holder.path.setTextColor(color)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.chuck_list_item_transaction, parent, false)
        return ViewHolder(itemView)
    }

    internal inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val code: TextView = view.findViewById(R.id.code)
        val path: TextView = view.findViewById(R.id.path)
        val host: TextView = view.findViewById(R.id.host)
        val start: TextView = view.findViewById(R.id.start)
        val duration: TextView = view.findViewById(R.id.duration)
        val size: TextView = view.findViewById(R.id.size)
        val ssl: ImageView = view.findViewById(R.id.ssl)
        var transaction: HttpTransaction? = null

    }
}
