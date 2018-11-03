package com.josedlpozo.galileo.chuck.internal.data

internal object HttpTransactionRepository {

    private const val MAX_SIZE = 300

    private val list : MutableList<HttpTransaction> = mutableListOf()

    fun add(transaction: HttpTransaction) {
        list.add(transaction)
        if (list.size > MAX_SIZE) {
            list.drop(list.size - MAX_SIZE)
        }
    }

    fun all(): List<HttpTransaction> = list

    fun find(id: Long) : HttpTransaction? = list.find { it.id == id }
}