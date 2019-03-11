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
import android.content.pm.ApplicationInfo



abstract class BaseFloatItem : FloatItem {

    var rootView: View? = null

    lateinit var layoutParams: WindowManager.LayoutParams
    lateinit var windowManager: WindowManager

    override fun performCreate(context: Context) {
        windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
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
        onViewCreated(rootView!!)
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

    abstract override fun onViewCreated(view: View)

    abstract override fun onCreateView(context: Context, view: ViewGroup?): View

    abstract override fun onLayoutParamsCreated(params: WindowManager.LayoutParams)

    abstract override fun onCreate(context: Context)

    open fun onDestroy() {
        windowManager.removeViewImmediate(rootView)
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

    fun getApplicationName(context: Context): String {
        val applicationInfo = context.applicationInfo
        val stringId = applicationInfo.labelRes
        return if (stringId == 0) applicationInfo.nonLocalizedLabel.toString() else context.getString(stringId)
    }
}