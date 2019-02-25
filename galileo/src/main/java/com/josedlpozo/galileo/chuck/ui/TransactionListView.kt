package com.josedlpozo.galileo.chuck.ui

import android.content.Context
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.josedlpozo.galileo.R
import com.josedlpozo.galileo.chuck.data.HttpTransactionRepository
import com.josedlpozo.galileo.chuck.support.FormatUtils
import com.josedlpozo.galileo.items.GalileoItem

internal class TransactionListView(context: Context) : RecyclerView(context) {

    private val adapter: TransactionAdapter by lazy {
        TransactionAdapter {
            TransactionActivity.start(context, it.id)
        }
    }

    init {
        setAdapter(adapter)
        layoutManager = LinearLayoutManager(context)
        addItemDecoration(DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL))

        val items = HttpTransactionRepository.all()
        adapter.refresh(items)
        if (items.isNotEmpty()) scrollToPosition(items.size - 1)
    }

}