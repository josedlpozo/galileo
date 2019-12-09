package com.josedlpozo.galileo.picker.overlays

import android.app.Activity
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
import android.os.Build
import androidx.core.app.NotificationCompat
import android.view.View
import android.view.ViewGroup
import com.josedlpozo.galileo.R
import com.josedlpozo.galileo.core.BaseFloatItem
import com.josedlpozo.galileo.picker.ui.DesignerTools
import com.josedlpozo.galileo.picker.utils.LaunchUtils
import com.josedlpozo.galileo.picker.utils.PreferenceUtils

internal class ColorPickerOverlay : BaseFloatItem() {

    companion object {
        private const val channel = "com.josedlpozo.galileo"
        private const val notificationId = 1002
        private const val ACTION_HIDE_PICKER = "hide_picker"
        private const val ACTION_SHOW_PICKER = "show_picker"
    }

    private lateinit var notificationManager: NotificationManager

    override fun onCreate(context: Context) {
        PreferenceUtils.ColorPickerPreferences.setColorPickerEnabled(context, false)
        view = ColorPickerOverlayView(context)
        layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        cancelNotification()
        handleVisibility()
        val prefs = PreferenceUtils.getShardedPreferences(context)
        prefs.registerOnSharedPreferenceChangeListener(preferenceChangeListener)
        super.onCreate(context)
    }

    override fun onViewCreated(view: View) {
        val filter = IntentFilter(ACTION_HIDE_PICKER)
        filter.addAction(ACTION_SHOW_PICKER)
        view.context.registerReceiver(receiver, filter)
        handleVisibility()
    }

    override fun onResume(activity: Activity) {
        super.onResume(activity)
        (view as ColorPickerOverlayView).setupMediaProjection()
        showNotification(notification(activity))
    }

    override fun onPaused() {
        super.onPaused()
        cancelNotification()
        (view as ColorPickerOverlayView).unregisterMediaProjection()
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
                                            1,
                                            Intent(if (isShown) ACTION_HIDE_PICKER else ACTION_SHOW_PICKER),
                                            0)
        val builder: NotificationCompat.Builder = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationCompat.Builder(context, channel)
        } else {
            NotificationCompat.Builder(context)
        }
        val text = context.getString(if (isShown) R.string.notif_content_hide_picker else R.string.notif_content_show_picker,
                                     getApplicationName(context))
        builder.setSmallIcon(if (isShown) R.drawable.ic_qs_colorpicker_on else R.drawable.ic_qs_colorpicker_off)
            .setContentTitle(context.getString(R.string.color_picker_qs_tile_label))
            .setContentText(text)
            .setStyle(NotificationCompat.BigTextStyle().bigText(text))
            .setContentIntent(pi)
        return builder.build()
    }

    private fun handleVisibility() {
        if (isActive()) show() else hide()
        view.context?.let { showNotification(notification(it)) }
    }

    private fun isActive(): Boolean = DesignerTools.colorPickerOn(view.context)

    private var receiver: BroadcastReceiver? = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            when {
                ACTION_HIDE_PICKER == action -> {
                    DesignerTools.setColorPickerOn(context, false)
                }
                ACTION_SHOW_PICKER == action -> {
                    DesignerTools.setColorPickerOn(context, true)
                    LaunchUtils.startColorPickerOrRequestPermission(context)
                }
            }

            handleVisibility()
        }
    }

    private val preferenceChangeListener = SharedPreferences.OnSharedPreferenceChangeListener { _, _ ->
        handleVisibility()
    }
}