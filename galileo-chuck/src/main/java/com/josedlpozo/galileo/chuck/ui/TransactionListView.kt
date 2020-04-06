package com.josedlpozo.galileo.chuck.ui

import android.content.Context
import com.josedlpozo.galileo.chuck.data.HttpTransactionRepository

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