package com.josedlpozo.galileo.chuck.ui

import android.content.Context
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import com.josedlpozo.galileo.R
import com.josedlpozo.galileo.chuck.data.HttpTransactionRepository
import com.josedlpozo.galileo.chuck.support.FormatUtils
import com.josedlpozo.galileo.items.GalileoItem

internal class TransactionListView(context: Context) : androidx.recyclerview.widget.RecyclerView(context) {

    private val adapter: TransactionAdapter by lazy {
        TransactionAdapter {
            TransactionActivity.start(context, it.id)
        }
    }

    init {
        setAdapter(adapter)
        layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
        addItemDecoration(
            androidx.recyclerview.widget.DividerItemDecoration(
                getContext(),
                androidx.recyclerview.widget.DividerItemDecoration.VERTICAL
            )
        )

        val items = HttpTransactionRepository.all()
        adapter.refresh(items)
        if (items.isNotEmpty()) scrollToPosition(items.size - 1)
    }

}