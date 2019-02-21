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

abstract class FloatItem {

    private var rootView: View? = null
    private lateinit var layoutParams: WindowManager.LayoutParams

    fun performCreate(context: Context) {
        onCreate(context)
        rootView = object : FrameLayout(context) {
            override fun dispatchKeyEvent(event: KeyEvent): Boolean {
                if (event.action == KeyEvent.ACTION_UP) {
                    if (event.keyCode == KeyEvent.KEYCODE_BACK || event.keyCode == KeyEvent.KEYCODE_HOME) {
                        return onBackPressed()
                    }
                }
                return super.dispatchKeyEvent(event)
            }
        }
        val view = onCreateView(context, rootView as ViewGroup)
        (rootView as ViewGroup).addView(view)
        onViewCreated(rootView)
        layoutParams = WindowManager.LayoutParams()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE
        }
        layoutParams.format = PixelFormat.TRANSPARENT
        layoutParams.gravity = Gravity.LEFT or Gravity.TOP
        onLayoutParamsCreated(layoutParams)
    }

    fun performDestroy() {
        rootView = null
        //onDestroy()
    }

    abstract fun onViewCreated(view: View?)

    abstract fun onCreateView(context: Context, view: ViewGroup?): View

    abstract fun onLayoutParamsCreated(params: WindowManager.LayoutParams)

    abstract fun onCreate(context: Context)

    //abstract fun onDestroy()

    protected fun <T : View> findViewById(@IdRes id: Int): T? = rootView?.findViewById(id)

    fun getRootView(): View? = rootView

    fun getLayoutParams(): WindowManager.LayoutParams = layoutParams


    open fun onEnterBackground() {

    }

    open fun onEnterForeground() {

    }

    protected fun onBackPressed(): Boolean = false
}