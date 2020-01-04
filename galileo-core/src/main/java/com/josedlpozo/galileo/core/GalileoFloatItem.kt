package com.josedlpozo.galileo.core

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.ViewGroup

abstract class GalileoFloatItem : FloatItem {

    lateinit var view: View
    lateinit var layoutParams: ViewGroup.LayoutParams

    override fun onCreate(context: Context) {
        onViewCreated(view)
    }

    override fun onResume(activity: Activity) {
        activity.window.addContentView(view, layoutParams)
    }

    override fun onPaused() {
        (view.parent as ViewGroup).removeView(view)
    }

    open fun show() {
        view.show()
    }

    open fun hide() {
        view.hide()
    }

    fun getApplicationName(context: Context): String {
        val applicationInfo = context.applicationInfo
        val stringId = applicationInfo.labelRes
        return if (stringId == 0) applicationInfo.nonLocalizedLabel.toString() else context.getString(
            stringId
        )
    }

    private fun View?.show() = this?.apply { visibility = View.VISIBLE }

    private fun View?.hide() = this?.apply { visibility = View.GONE }
}