package com.josedlpozo.galileo.core

import android.app.Activity
import android.content.Context
import android.view.View

interface FloatItem {

    fun onViewCreated(view: View)

    fun onCreate(context: Context)

    fun onResume(activity: Activity)

    fun onPaused()

}