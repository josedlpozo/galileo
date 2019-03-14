package com.josedlpozo.galileo.common

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

abstract class BaseFloatItem : FloatItem {

    var rootView: View? = null

    lateinit var layoutParams: ViewGroup.LayoutParams

    override fun performCreate(context: Context) {
        onCreate(context)
        rootView = onCreateView(context)
        onViewCreated(rootView!!)
        layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE
        }
        layoutParams.format = PixelFormat.TRANSPARENT
        layoutParams.gravity = Gravity.LEFT or Gravity.TOP
        onLayoutParamsCreated(layoutParams)*/
    }

    abstract override fun onViewCreated(view: View)

    abstract override fun onCreateView(context: Context): View

    abstract override fun onLayoutParamsCreated(params: WindowManager.LayoutParams)

    abstract override fun onCreate(context: Context)

    open fun onDestroy() {
        //windowManager.removeViewImmediate(rootView)
        rootView = null
    }

    override fun <T : View> findViewById(@IdRes id: Int): T? = rootView?.findViewById(id)


    override fun onEnterBackground() {

    }

    override fun onEnterForeground() {

    }

    override fun onBackPressed(): Boolean = false

    open fun show() {
        rootView.show()
    }

    open fun hide() {
        rootView.hide()
    }
}