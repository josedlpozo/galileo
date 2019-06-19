package com.josedlpozo.galileo.picker.overlays

import android.app.*
import android.content.*
import android.graphics.Color
import android.os.Build
import android.support.v4.app.NotificationCompat
import android.view.View
import android.view.ViewGroup
import com.josedlpozo.galileo.R
import com.josedlpozo.galileo.common.BaseFloatItem
import com.josedlpozo.galileo.picker.ui.DesignerTools
import com.josedlpozo.galileo.picker.utils.PreferenceUtils

class GridOverlay : BaseFloatItem() {

    companion object {
        private const val channel = "com.josedlpozo.galileo"
        private const val notificationId = 1001
        private const val ACTION_HIDE_OVERLAY = "hide_overlay"
        private const val ACTION_SHOW_OVERLAY = "show_overlay"
    }

    private lateinit var notificationManager: NotificationManager

    override fun onCreate(context: Context) {
        PreferenceUtils.GridPreferences.setGridEnabled(context, false)
        view = GridOverlayView(context)
        layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        cancelNotification()
        handleVisibility()
        val prefs = PreferenceUtils.getShardedPreferences(context)
        prefs.registerOnSharedPreferenceChangeListener(preferenceChangeListener)
        super.onCreate(context)
    }

    override fun onViewCreated(view: View) {
        val filter = IntentFilter(ACTION_HIDE_OVERLAY)
        filter.addAction(ACTION_SHOW_OVERLAY)
        view.context.registerReceiver(receiver, filter)
        handleVisibility()
    }

    override fun onResume(activity: Activity) {
        super.onResume(activity)
        showNotification(notification(activity))
    }

    override fun onPaused() {
        super.onPaused()
        cancelNotification()
    }

    private fun cancelNotification() {
        notificationManager.cancel(notificationId)
    }

    private fun showNotification(notification: Notification) {
        notificationManager.notify(notificationId, notification)
    }

    private fun notification(context: Context): Notification {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelName = "My Background Service"
            val chan = NotificationChannel(channel, channelName, NotificationManager.IMPORTANCE_LOW)
            chan.lightColor = Color.BLUE
            chan.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
            notificationManager.createNotificationChannel(chan)
        }
        val isShown = view.isShown || isActive()
        val pi = PendingIntent.getBroadcast(context,
                0,
                Intent(if (isShown) ACTION_HIDE_OVERLAY else ACTION_SHOW_OVERLAY),
                0)
        val builder: NotificationCompat.Builder = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationCompat.Builder(context, channel)
        } else {
            NotificationCompat.Builder(context)
        }
        val text = context.getString(if (isShown) R.string.notif_content_hide_grid_overlay else R.string.notif_content_show_grid_overlay, getApplicationName(context))
        builder.setSmallIcon(if (isShown) R.drawable.ic_qs_grid_on_vector else R.drawable.ic_qs_grid_off_vector)
                .setContentTitle(context.getString(R.string.grid_qs_tile_label))
                .setContentText(text)
                .setStyle(NotificationCompat.BigTextStyle().bigText(text))
                .setContentIntent(pi)
        return builder.build()
    }

    private var receiver: BroadcastReceiver? = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            when {
                ACTION_HIDE_OVERLAY == action -> DesignerTools.setGridOverlayOn(context, false)
                ACTION_SHOW_OVERLAY == action -> DesignerTools.setGridOverlayOn(context, true)
            }

            handleVisibility()
        }
    }

    private fun handleVisibility() {
        if (isActive()) show() else hide()
        view.context?.let { showNotification(notification(it)) }
    }

    private fun isActive(): Boolean {
        return DesignerTools.gridOverlayOn(view.context)
    }

    private val preferenceChangeListener = SharedPreferences.OnSharedPreferenceChangeListener { _, _ ->
        handleVisibility()
    }
}