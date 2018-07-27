package com.readystatesoftware.chuck.internal.ui

import android.content.Context
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.josedlpozo.galileo.items.GalileoItem

class TransactionListView(context: Context) : RecyclerView(context), GalileoItem {

    private val adapter: TransactionAdapter by lazy {
        TransactionAdapter(context, TransactionListFragment.OnListFragmentInteractionListener {

        })
    }

    init {
        setAdapter(adapter)
        layoutManager = LinearLayoutManager(context)
        addItemDecoration(DividerItemDecoration(getContext()!!, DividerItemDecoration.VERTICAL))
    }

    override val view: View = this

    override val name: String = "Chuck"

    override val icon: Int
        get() = android.R.drawable.stat_sys_warning

    override fun snapshot(): String = ""

}