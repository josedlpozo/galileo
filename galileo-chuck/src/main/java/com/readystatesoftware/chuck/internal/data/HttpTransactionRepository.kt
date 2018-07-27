package com.readystatesoftware.chuck.internal.data

import android.arch.lifecycle.MutableLiveData

object HttpTransactionRepository {

    var data: MutableLiveData<List<HttpTransaction>> = MutableLiveData()
        private set

    private val list : MutableList<HttpTransaction> = mutableListOf()

    fun add(transaction: HttpTransaction) {
        list.add(transaction)
        data.postValue(list.toList())
    }

}