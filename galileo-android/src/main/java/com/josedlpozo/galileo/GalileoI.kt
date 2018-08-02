package com.josedlpozo.galileo

import com.readystatesoftware.chuck.GalileoChuckInterceptor
import okhttp3.Interceptor

object GalileoI {

    val interceptor: Interceptor = GalileoChuckInterceptor.getInstance()
}