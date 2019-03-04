package com.josedlpozo.galileo.common

import android.content.Context
import android.support.annotation.IdRes
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager

interface FloatItem {

    fun performCreate(context: Context)

    fun performDestroy()

    fun onViewCreated(view: View)

    fun onCreateView(context: Context, view: ViewGroup?): View

    fun onLayoutParamsCreated(params: WindowManager.LayoutParams)

    fun onCreate(context: Context)

    fun <T : View> findViewById(@IdRes id: Int): T?


    fun onEnterBackground() {

    }

    fun onEnterForeground() {

    }

    fun onBackPressed(): Boolean = false

}