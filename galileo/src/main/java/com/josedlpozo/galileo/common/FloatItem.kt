package com.josedlpozo.galileo.common

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.res.Resources
import android.graphics.PixelFormat
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.support.annotation.IdRes
import android.support.annotation.StringRes
import android.view.Gravity
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout

abstract class FloatItem {

    private var rootView: View? = null
    private lateinit var layoutParams: WindowManager.LayoutParams
    private var handler: Handler? = null
    private val receiver = InnerReceiver()

    private var bundle: Bundle? = null

    fun performCreate(context: Context) {
        handler = Handler(Looper.myLooper())
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
        val intentFilter = IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)
        context.registerReceiver(receiver, intentFilter)
    }

    fun performDestroy() {
        getContext()?.unregisterReceiver(receiver)
        handler = null
        rootView = null
        onDestroy()
    }

    fun getContext(): Context? = rootView?.context

    fun getResources(): Resources? = getContext()?.resources

    fun getString(@StringRes resId: Int): String? = getContext()?.getString(resId)

    open fun onViewCreated(view: View?) {

    }

    protected abstract fun onCreateView(context: Context, view: ViewGroup?): View

    open fun onLayoutParamsCreated(params: WindowManager.LayoutParams) {

    }

    open fun onCreate(context: Context) {

    }

    open fun onDestroy() {

    }

    fun isShow(): Boolean {
        return rootView?.isShown == true
    }

    protected fun <T : View> findViewById(@IdRes id: Int): T? = rootView?.findViewById(id)

    fun getRootView(): View? = rootView

    fun getLayoutParams(): WindowManager.LayoutParams = layoutParams

    fun finish() {
        //FloatPageManager.getInstance().remove(this)
    }

    open fun onEnterBackground() {

    }

    open fun onEnterForeground() {

    }

    fun onHomeKeyPress() {}

    fun onRecentAppKeyPress() {}

    protected fun onBackPressed(): Boolean {
        return false
    }

    private inner class InnerReceiver : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            if (Intent.ACTION_CLOSE_SYSTEM_DIALOGS == action) {
                val reason = intent.getStringExtra(SYSTEM_DIALOG_REASON_KEY)
                if (reason != null) {
                    if (reason == SYSTEM_DIALOG_REASON_HOME_KEY) {
                        onHomeKeyPress()
                    } else if (reason == SYSTEM_DIALOG_REASON_RECENT_APPS) {
                        onRecentAppKeyPress()
                    }
                }
            }
        }
    }

    fun setBundle(bundle: Bundle) {
        this.bundle = bundle
    }

    fun getBundle(): Bundle? = bundle

    companion object {
        internal const val SYSTEM_DIALOG_REASON_KEY = "reason"

        internal const val SYSTEM_DIALOG_REASON_RECENT_APPS = "recentapps"

        internal const val SYSTEM_DIALOG_REASON_HOME_KEY = "homekey"
    }
}