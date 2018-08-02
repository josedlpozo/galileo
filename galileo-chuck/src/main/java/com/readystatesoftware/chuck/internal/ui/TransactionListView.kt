package com.readystatesoftware.chuck.internal.ui

import android.content.Context
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.josedlpozo.galileo.items.GalileoItem
import com.readystatesoftware.chuck.R
import com.readystatesoftware.chuck.internal.data.HttpTransactionRepository
import com.readystatesoftware.chuck.internal.support.FormatUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class TransactionListView(context: Context) : RecyclerView(context), GalileoItem {

    private val adapter: TransactionAdapter by lazy {
        TransactionAdapter(context, TransactionListFragment.OnListFragmentInteractionListener {
            TransactionActivity.start(context, it.id)
        })
    }

    init {
        setAdapter(adapter)
        layoutManager = LinearLayoutManager(context)
        addItemDecoration(DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL))

        adapter.refresh(HttpTransactionRepository.all())
        HttpTransactionRepository.data.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe {
            adapter.refresh(it)
            smoothScrollToPosition(it.size - 1)
        }
    }

    override val view: View = this

    override val name: String = "Chuck"

    override val icon: Int
        get() = R.drawable.ic_http_request

    override fun snapshot(): String = HttpTransactionRepository.all().joinToString("\n", transform = {
        FormatUtils.getShareText(context, it)
    }, postfix = "\n\n")

}