package com.josedlpozo.galileo.picker.overlays

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.*
import android.graphics.Color
import android.graphics.PixelFormat
import android.os.Build
import android.support.v4.app.NotificationCompat
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.josedlpozo.galileo.R
import com.josedlpozo.galileo.common.BaseFloatItem
import com.josedlpozo.galileo.picker.overlays.GridOverlayService.Companion.ACTION_HIDE_OVERLAY
import com.josedlpozo.galileo.picker.overlays.GridOverlayService.Companion.ACTION_SHOW_OVERLAY
import com.josedlpozo.galileo.picker.qs.GridQuickSettingsTile
import com.josedlpozo.galileo.picker.ui.DesignerTools
import com.josedlpozo.galileo.picker.utils.PreferenceUtils

class GridOverlay : BaseFloatItem() {

    companion object {
        val channel = "com.josedlpozo.galileo"
        private val notificationId = this.javaClass.hashCode()
    }

    private lateinit var notificationManager: NotificationManager

    override fun onViewCreated(view: View) {
        val filter = IntentFilter(GridQuickSettingsTile.ACTION_TOGGLE_STATE)
        filter.addAction(GridQuickSettingsTile.ACTION_UNPUBLISH)
        filter.addAction(ACTION_HIDE_OVERLAY)
        filter.addAction(ACTION_SHOW_OVERLAY)
        view.context.registerReceiver(receiver, filter)
        handleVisibility()
    }

    override fun onCreateView(context: Context, view: ViewGroup?): View = GridOverlayView(context)

    override fun onLayoutParamsCreated(params: WindowManager.LayoutParams) {
        params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
        params.width = ViewGroup.LayoutParams.MATCH_PARENT
        params.height = ViewGroup.LayoutParams.MATCH_PARENT
        params.format = PixelFormat.TRANSLUCENT
    }

    override fun onCreate(context: Context) {
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
        notificationManager.cancel(notificationId)
        val context = rootView?.context ?: return
        val prefs = PreferenceUtils.getShardedPreferences(context)
        prefs.unregisterOnSharedPreferenceChangeListener(preferenceChangeListener)
        super.onDestroy()
    }

    private fun showNotification(notification: Notification) {
        notificationManager.notify(notificationId, notification)
    }

    private fun notification(context: Context): Notification {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelName = "My Background Service"
            val chan = NotificationChannel(channel, channelName, NotificationManager.IMPORTANCE_NONE)
            chan.lightColor = Color.BLUE
            chan.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
            notificationManager.createNotificationChannel(chan)
        }
        val isShown = rootView?.isShown == true || isActive()
        val pi = PendingIntent.getBroadcast(context,
                0,
                Intent(if (isShown) GridOverlayService.ACTION_HIDE_OVERLAY else GridOverlayService.ACTION_SHOW_OVERLAY),
                0)
        val builder: NotificationCompat.Builder = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationCompat.Builder(context, channel)
        } else {
            NotificationCompat.Builder(context)
        }
        val text = context.getString(if (isShown) R.string.notif_content_hide_grid_overlay else R.string.notif_content_show_grid_overlay)
        builder.setSmallIcon(if (isShown) R.drawable.ic_qs_grid_on else R.drawable.ic_qs_grid_off)
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
            } else if (ACTION_HIDE_OVERLAY == action) {
                DesignerTools.setGridOverlayOn(context, false)
            } else if (ACTION_SHOW_OVERLAY == action) {
                DesignerTools.setGridOverlayOn(context, true)
            }

            handleVisibility()
        }
    }

    private fun handleVisibility() {
        if (isActive()) show() else hide()
        rootView?.context?.let { showNotification(notification(it)) }
    }

    private fun isActive(): Boolean {
        val context = rootView?.context ?: return false
        return DesignerTools.gridOverlayOn(context)
    }

    private val preferenceChangeListener = SharedPreferences.OnSharedPreferenceChangeListener { _, _ ->
        handleVisibility()
    }
}