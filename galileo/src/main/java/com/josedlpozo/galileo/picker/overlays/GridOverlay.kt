package com.josedlpozo.galileo.picker.overlays

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.PixelFormat
import android.os.Build
import android.support.v4.app.NotificationCompat
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.josedlpozo.galileo.R
import com.josedlpozo.galileo.common.BaseFloatItem
import com.josedlpozo.galileo.picker.overlays.GridOverlayService.Companion
import com.josedlpozo.galileo.picker.qs.GridQuickSettingsTile
import com.josedlpozo.galileo.picker.ui.DesignerTools
import com.josedlpozo.galileo.picker.utils.PreferenceUtils

class GridOverlay : BaseFloatItem() {

    val channel = "com.josedlpozo.galileo"

    private lateinit var windowManager: WindowManager
    private lateinit var notificationManager: NotificationManager

    override fun onViewCreated(view: View) {
        if (isActive()) showNotification(notification(view.context))

        val filter = IntentFilter(GridQuickSettingsTile.ACTION_TOGGLE_STATE)
        filter.addAction(GridQuickSettingsTile.ACTION_UNPUBLISH)
        filter.addAction(Companion.ACTION_HIDE_OVERLAY)
        filter.addAction(Companion.ACTION_SHOW_OVERLAY)
        view.context.registerReceiver(receiver, filter)
    }

    override fun onCreateView(context: Context, view: ViewGroup?): View = GridOverlayView(context)

    override fun onLayoutParamsCreated(params: WindowManager.LayoutParams) {
        params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
        params.width = ViewGroup.LayoutParams.MATCH_PARENT
        params.height = ViewGroup.LayoutParams.MATCH_PARENT
        params.format = PixelFormat.TRANSLUCENT
    }

    override fun onCreate(context: Context) {
        windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        handleVisibility()
        val prefs = PreferenceUtils.getShardedPreferences(context)
        prefs.registerOnSharedPreferenceChangeListener(preferenceChangeListener)
    }

    override fun onEnterForeground() {
        super.onEnterForeground()
        handleVisibility()
    }

    override fun onEnterBackground() {
        super.onEnterBackground()
        handleVisibility()
    }

    override fun onDestroy() {
        val context = rootView?.context ?: return
        val prefs = PreferenceUtils.getShardedPreferences(context)
        prefs.unregisterOnSharedPreferenceChangeListener(preferenceChangeListener)
    }

    override fun show() {
        super.show()
    }

    private fun showNotification(notification: Notification) {
        notificationManager.notify(0, notification)
    }

    private fun notification(context: Context): Notification {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelName = "My Background Service"
            val chan = NotificationChannel(channel, channelName, NotificationManager.IMPORTANCE_NONE)
            chan.lightColor = Color.BLUE
            chan.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
            notificationManager.createNotificationChannel(chan)
        }
        val pi = PendingIntent.getBroadcast(context,
                                            0,
                                            Intent(if (isActive()) GridOverlayService.ACTION_HIDE_OVERLAY else GridOverlayService.ACTION_SHOW_OVERLAY),
                                            0)
        val builder: NotificationCompat.Builder = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationCompat.Builder(context, channel)
        } else {
            NotificationCompat.Builder(context)
        }
        val text = context.getString(if (isActive()) R.string.notif_content_hide_grid_overlay else R.string.notif_content_show_grid_overlay)
        builder.setSmallIcon(if (isActive()) R.drawable.ic_qs_grid_on else R.drawable.ic_qs_grid_off)
            .setContentTitle(context.getString(R.string.grid_qs_tile_label))
            .setContentText(text)
            .setStyle(NotificationCompat.BigTextStyle().bigText(text))
            .setContentIntent(pi)
        return builder.build()
    }

    private var receiver: BroadcastReceiver? = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            if (GridQuickSettingsTile.ACTION_UNPUBLISH == action) {
                handleVisibility()
            } else if (GridQuickSettingsTile.ACTION_TOGGLE_STATE == action) {
                handleVisibility()
            } else if (Companion.ACTION_HIDE_OVERLAY == action) {
                hide()
            } else if (Companion.ACTION_SHOW_OVERLAY == action) {
                show()
            }
        }
    }

    private fun handleVisibility() {
        if (isActive()) show() else hide()
    }

    private fun isActive(): Boolean {
        val context = rootView?.context ?: return false
        return DesignerTools.gridOverlayOn(context)
    }

    private val preferenceChangeListener = SharedPreferences.OnSharedPreferenceChangeListener { prefs, key ->
        handleVisibility()
    }
}