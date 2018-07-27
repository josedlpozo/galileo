package com.readystatesoftware.chuck.internal.data

import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject


object HttpTransactionRepository {

    val data: Subject<List<HttpTransaction>> = PublishSubject.create()

    private val list : MutableList<HttpTransaction> = mutableListOf()

    fun add(transaction: HttpTransaction) {
        list.add(transaction)
        data.onNext(list)
    }

}