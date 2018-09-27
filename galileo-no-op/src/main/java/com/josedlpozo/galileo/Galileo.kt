package com.josedlpozo.galileo

import android.content.Context
import okhttp3.Interceptor

class Galileo(private val context: Context) {

    fun start() {}

    fun stop() {}

    companion object {
        val interceptor : Interceptor = Interceptor { it.proceed(it.request()) }

        val interceptorOld: com.squareup.okhttp.Interceptor = com.squareup.okhttp.Interceptor { it.proceed(it.request()) }
    }
}