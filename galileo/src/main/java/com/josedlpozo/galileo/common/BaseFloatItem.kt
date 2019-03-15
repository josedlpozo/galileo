package com.josedlpozo.galileo.common

import android.app.Activity
import android.content.Context
import android.graphics.PixelFormat
import android.os.Build
import android.support.annotation.IdRes
import android.view.Gravity
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout
import com.josedlpozo.galileo.parent.extensions.hide
import com.josedlpozo.galileo.parent.extensions.show
import android.content.pm.ApplicationInfo

abstract class BaseFloatItem : FloatItem {

    lateinit var view : View
    lateinit var layoutParams: ViewGroup.LayoutParams

    override fun onCreate(context: Context) {
        onViewCreated(view)
    }

    override fun onResume(activity: Activity) {
        activity.window.addContentView(view, layoutParams)
    }

    override fun onPaused() {}

    override fun onEnterBackground() {

    }

    override fun onEnterForeground() {

    }

    override fun onBackPressed(): Boolean = false

    open fun show() {
        view.show()
    }

    open fun hide() {
        view.hide()
    }

    fun getApplicationName(context: Context): String {
        val applicationInfo = context.applicationInfo
        val stringId = applicationInfo.labelRes
        return if (stringId == 0) applicationInfo.nonLocalizedLabel.toString() else context.getString(stringId)
    }
}