package com.josedlpozo.galileo.chuck.ui

import android.content.Context
import android.view.View
import com.josedlpozo.galileo.chuck.R
import com.josedlpozo.galileo.chuck.data.HttpTransactionRepository
import com.josedlpozo.galileo.chuck.support.FormatUtils
import com.josedlpozo.galileo.core.GalileoItem

class TransactionGalileoItem(private val context: Context) :
    GalileoItem {

    private val view: TransactionListView by lazy { TransactionListView(context) }

    override val name: String = "Chuck"

    override val icon: Int = R.drawable.ic_http_request

    override fun snapshot(): String {
        val transactions = HttpTransactionRepository.all()
        return transactions.joinToString("\n\n\n\n", transform = {
            "===================================================\n" +
                    "===================================================\n\n" +
                    FormatUtils.getShareText(it) + "\n\n" +
                    "===================================================\n" +
                    "===================================================\n"
        }, postfix = "\n\n")
    }

    override fun view(): View = view
}