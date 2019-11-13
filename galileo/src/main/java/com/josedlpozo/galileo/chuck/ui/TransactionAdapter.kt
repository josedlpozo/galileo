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

import androidx.core.content.ContextCompat
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.josedlpozo.galileo.R
import com.josedlpozo.galileo.chuck.data.HttpTransaction
import com.josedlpozo.galileo.parent.extensions.padding
import com.josedlpozo.galileo.parent.extensions.tint
import java.util.*

internal class TransactionAdapter(private val listener: (HttpTransaction) -> Unit) : androidx.recyclerview.widget.RecyclerView.Adapter<TransactionAdapter.ViewHolder>() {
    private var list: List<HttpTransaction> = listOf()

    init {
        list = ArrayList()
    }

    internal fun refresh(updatedList: List<HttpTransaction>) {
        val diffUtil = object : DiffUtil.Callback() {

            override fun getOldListSize(): Int {
                return list.size
            }

            override fun getNewListSize(): Int {
                return updatedList.size
            }

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                if (oldItemPosition >= list.size || newItemPosition >= updatedList.size) return false
                val oldItem = list[oldItemPosition]
                val newItem = updatedList[newItemPosition]
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                if (oldItemPosition >= list.size || newItemPosition >= updatedList.size) return false
                val oldItem = list[oldItemPosition]
                val newItem = updatedList[newItemPosition]
                return oldItem == newItem
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
        holder.bind(transaction)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.chuck_list_item_transaction, parent, false)
        return ViewHolder(itemView)
    }

    internal inner class ViewHolder(val view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {

        private val colorDefault: Int = ContextCompat.getColor(view.context, R.color.galileocolor_status_default)
        private val colorRequested: Int = ContextCompat.getColor(view.context, R.color.galileocolor_status_requested)
        private val colorError: Int = ContextCompat.getColor(view.context, R.color.galileocolor_status_error)
        private val color500: Int = ContextCompat.getColor(view.context, R.color.galileocolor_status_500)
        private val color400: Int = ContextCompat.getColor(view.context, R.color.galileocolor_status_400)
        private val color300: Int = ContextCompat.getColor(view.context, R.color.galileocolor_status_300)

        private val code: TextView = view.findViewById(R.id.code)
        private val method: TextView = view.findViewById(R.id.method)
        private val path: TextView = view.findViewById(R.id.path)
        private val host: TextView = view.findViewById(R.id.host)
        private val start: TextView = view.findViewById(R.id.start)
        private val duration: TextView = view.findViewById(R.id.duration)
        private val size: TextView = view.findViewById(R.id.size)
        private val ssl: ImageView = view.findViewById(R.id.ssl)

        fun bind(transaction: HttpTransaction) {
            method.text = transaction.method
            path.text = transaction.path
            host.text = transaction.host
            start.text = transaction.requestStartTimeString
            ssl.visibility = if (transaction.isSsl) View.VISIBLE else View.GONE
            if (transaction.status == HttpTransaction.Status.Complete) {
                code.text = transaction.responseCode.toString()
                duration.text = transaction.durationString
                size.text = transaction.totalSizeString
            } else {
                code.text = null
                duration.text = null
                size.text = null
            }
            if (transaction.status == HttpTransaction.Status.Failed) {
                code.text = "!!!"
            }
            setStatusColor(transaction)
            view.setOnClickListener {
                this@TransactionAdapter.listener.invoke(transaction)
            }
        }

        private fun setStatusColor(transaction: HttpTransaction) {
            val color: Int = when {
                transaction.status == HttpTransaction.Status.Failed -> colorError
                transaction.status == HttpTransaction.Status.Requested -> colorRequested
                transaction.responseCode >= 500 -> color500
                transaction.responseCode >= 400 -> color400
                transaction.responseCode >= 300 -> color300
                else -> colorDefault
            }
            val tintedDrawable = AppCompatResources.getDrawable(itemView.context, R.drawable.rounded_corner)?.tint(color)
            code.setBackgroundDrawable(tintedDrawable)

            code.padding(R.dimen.galileo_margin_minimum)
            path.setTextColor(color)
            method.setTextColor(color)
        }
    }
}
