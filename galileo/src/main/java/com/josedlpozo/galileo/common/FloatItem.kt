package com.josedlpozo.galileo.common

import android.app.Activity
import android.content.Context
import android.support.annotation.IdRes
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager

interface FloatItem {

    fun onViewCreated(view: View)

    fun onCreate(context: Context)

    fun onEnterBackground() {

    }

    fun onEnterForeground() {

    }

    fun onBackPressed(): Boolean = false

    fun onResume(activity: Activity)

    fun onPaused()

}