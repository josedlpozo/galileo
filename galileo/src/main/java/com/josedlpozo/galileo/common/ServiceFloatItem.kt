package com.josedlpozo.galileo.common

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.support.annotation.IdRes
import android.view.*
import android.widget.FrameLayout
import com.josedlpozo.galileo.parent.extensions.hide
import com.josedlpozo.galileo.parent.extensions.show

abstract class ServiceFloatItem : Service(), FloatItem {

    val channel = "com.josedlpozo.galileo"

    abstract val notificationId: Int
    abstract val hasToNotify: Boolean

    override fun onBind(p0: Intent?): IBinder? = null

    var rootView: View? = null

    lateinit var layoutParams: WindowManager.LayoutParams

    override fun performCreate(context: Context) {
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

    override fun performDestroy() {
        rootView = null
        //onDestroy()
    }

    open fun setup(context: Context) {
        if (!hasToNotify) return
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelName = "My Background Service"
            val chan = NotificationChannel(channel, channelName, NotificationManager.IMPORTANCE_NONE)
            chan.lightColor = Color.BLUE
            chan.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
            val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(chan)
        }

        startForeground(notificationId, getPersistentNotification())
    }

    open fun getPersistentNotification(): Notification = Notification()

    abstract override fun onViewCreated(view: View?)

    abstract override fun onCreateView(context: Context, view: ViewGroup?): View

    abstract override fun onLayoutParamsCreated(params: WindowManager.LayoutParams)

    override fun onCreate(context: Context) {
        setup(context)
    }

    //abstract fun onDestroy()

    override fun <T : View> findViewById(@IdRes id: Int): T? = rootView?.findViewById(id)


    override fun onEnterBackground() {

    }

    override fun onEnterForeground() {

    }

    override fun onBackPressed(): Boolean = false

    fun show() {
        rootView.show()
    }

    fun hide() {
        rootView.hide()
    }

}